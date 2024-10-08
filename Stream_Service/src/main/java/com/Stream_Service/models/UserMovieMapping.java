package com.Stream_Service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_movie_mapping")
public class UserMovieMapping implements Persistable<Long> {
    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("movie_id")
    private Long movieId;

    @Transient
    private boolean isNew;
}
