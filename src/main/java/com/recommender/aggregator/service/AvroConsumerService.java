package com.recommender.aggregator.service;

import com.recommender.aggregator.entity.MovieEntity;
import com.recommender.aggregator.entity.RatingEntity;
import com.recommender.aggregator.entity.UserEntity;
import com.recommender.aggregator.repository.GenreRepository;
import com.recommender.aggregator.repository.MovieRepository;
import com.recommender.aggregator.repository.RatingRepository;
import com.recommender.aggregator.repository.UserRepository;
import com.recommender.avro.Movie;
import com.recommender.avro.Rating;
import com.recommender.avro.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AvroConsumerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private RatingRepository ratingRepository;


    @KafkaListener(id = "userListenerId", topics = "${kafka.movie-topic:movie-topic}", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserEvent(User userRecord) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userRecord.getUserId());
        List<CharSequence> preferredGenres = userRecord.getPreferredGenres();
        userEntity.setPreferredGenres(
                preferredGenres.stream()
                        .map(CharSequence::toString)
                        .map(genre -> genreRepository.findById(genre).orElseThrow(() -> new RuntimeException("Genre not found")))
                        .map(userEntity::addGenre)
                        .toList()
        );
        userRepository.save(userEntity);
        log.info("Consumed user record: {}", userRecord);
    }

    @KafkaListener(id = "movieListenerId", topics = "${kafka.movie-topic:movie-topic}", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeMovieEvent(Movie movieRecord) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(movieRecord.getMovieId());
        List<CharSequence> genres = movieRecord.getGenres();
        movieEntity.setGenres(
                genres.stream()
                        .map(CharSequence::toString)
                        .map(genre -> genreRepository.findById(genre).orElseThrow(() -> new RuntimeException("Genre not found")))
                        .map(movieEntity::addGenre)
                        .toList()
        );
        movieRepository.save(movieEntity);
        log.info("Consumed movie record: {}", movieRecord);
    }

    @KafkaListener(id = "ratingListenerId", topics = "${kafka.rating-topic:rating-topic}", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeRatingEvent(Rating ratingRecord) {
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setRating(ratingRecord.getRatingValue());
        ratingEntity.setMovie(movieRepository.findById(ratingRecord.getMovieId()).orElseThrow(() -> new RuntimeException("Movie not found")));
        ratingEntity.setUser(userRepository.findById(ratingRecord.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        ratingRepository.save(ratingEntity);
        log.info("Consumed rating record: {}", ratingRecord);
    }

}
