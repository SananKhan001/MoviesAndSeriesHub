package com.Stream_Service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.Identifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "episodes")
public class Episodes implements Persistable<Long>{
    @Id
    @Column("id")
    private Long id;

    @Column("unique_poster_id")
    private String uniquePosterId; // varchar(255)

    @Column("belongs_to_movie")
    private Long movieId; // bigint

    @Column("belongs_to_series")
    private Long seriesId; // bigint
    @Transient
    private boolean isNew;
}
