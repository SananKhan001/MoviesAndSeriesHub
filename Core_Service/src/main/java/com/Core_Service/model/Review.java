package com.Core_Service.model;

import com.Core_Service.model_response.ReviewResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "reviews")
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "viewer")
    private Viewer viewer;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "rating", nullable = false)
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "review_for_movie")
    private Movie reviewForMovie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "review_for_series")
    private Series reviewForSeries;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    public ReviewResponse to() {
        return ReviewResponse.builder()
                .id(this.id)
                .viewerId(this.viewer.getId())
                .comment(this.comment)
                .rating(this.rating).build();
    }
}
