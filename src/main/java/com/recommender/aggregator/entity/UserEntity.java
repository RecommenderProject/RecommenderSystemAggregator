package com.recommender.aggregator.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@ToString
@Table(name = "users")
public class UserEntity {

    @Id
    private Long id;

    @ManyToMany
    @JoinTable(name = "user_to_genre",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_name"))
    @JsonManagedReference
    List<GenreEntity> preferredGenres;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<RatingEntity> ratings;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public GenreEntity addGenre(GenreEntity genre) {
        preferredGenres.add(genre);
        return genre;
    }
}
