package com.Core_Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "viewer")
    private Viewer viewer;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "rating", nullable = false)
    private int rating;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "review_for_movie")
    private Movie reviewForMovie;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "review_for_series")
    private Series reviewForSeries;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
}
