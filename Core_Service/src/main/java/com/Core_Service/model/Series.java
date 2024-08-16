package com.Core_Service.model;

import com.Core_Service.helpers.StreamServiceDetails;
import com.Core_Service.model_response.SeriesResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "series")
public class Series implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "series_name", nullable = false, unique = true)
    private String name;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "series_description", nullable = false, length = 1200)
    private String description;

    // uuid
    @Column(name = "unique_poster_id", nullable = false)
    private String uniquePosterId;

    @Column(name = "series_price", nullable = false)
    private int price;

    @OneToMany(mappedBy = "reviewForSeries", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews;

    @Column(name = "overall_rating")
    private Double rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "series_viewer_mapping",
            joinColumns = @JoinColumn(name = "series_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "viewer_id", referencedColumnName = "id")
    )
    private List<Viewer> viewers;

    @OneToMany(mappedBy = "belongsToSeries", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Episode> episode;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    public SeriesResponse to(){
        return SeriesResponse.builder().seriesId(this.id)
                .name(this.name).genre(this.genre)
                .description(this.description)
                .uniqueSeriesId(this.uniquePosterId)
                .posterURL(StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + this.uniquePosterId)
                .price(this.price).rating(this.rating == null ? -1 : this.rating)
                .createdAt(this.createdAt.toString())
                .build();
    }
}
