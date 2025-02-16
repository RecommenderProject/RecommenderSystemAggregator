CREATE TABLE IF NOT EXISTS ratings(
    user_id BIGINT NOT NULL REFERENCES users(id),
    movie_id BIGINT NOT NULL REFERENCES movies(id),
    rating DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    primary key (user_id, movie_id)
);