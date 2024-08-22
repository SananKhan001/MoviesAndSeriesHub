package com.payment.service.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "series_user_mapping")
public class SeriesUserMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "series_id", nullable = false)
    private Long seriesId;

}
