package com.recommender.aggregator.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@ToString
public class MovieEntity {

    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "movie_to_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_name")
    )
    List<GenreEntity> genres;

    @OneToMany(mappedBy = "movie")
    @JsonManagedReference
    private List<RatingEntity> ratings;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public GenreEntity addGenre(GenreEntity genre) {
        genres.add(genre);
        return genre;
    }
}
