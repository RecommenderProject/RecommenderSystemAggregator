package com.recommender.aggregator.repository;

import com.recommender.aggregator.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, RatingEntity> {
}
