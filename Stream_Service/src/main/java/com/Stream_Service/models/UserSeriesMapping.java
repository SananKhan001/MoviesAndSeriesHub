package com.Stream_Service.models;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user_series_mapping")
public class UserSeriesMapping {
    @Column("user_id")
    private Long userId;

    @Column("series_id")
    private Long seriesId;
}
