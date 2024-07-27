package com.Stream_Service.models;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user_movie_mapping")
public class UserMovieMapping {
    @Column("user_id")
    private Long userId;
    @Column("movie_id")
    private Long movieId;
}
