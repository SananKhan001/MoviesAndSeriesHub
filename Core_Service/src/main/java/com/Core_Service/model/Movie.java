package com.Core_Service.model;

import com.Core_Service.helpers.StreamServiceDetails;
import com.Core_Service.model_response.MovieResponse;
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
@Table(name = "movies")
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "movie_name", nullable = false, unique = true)
    private String name;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "movie_description", nullable = false, length = 1200)
    private String description;

    // uuid
    @Column(name = "unique_poster_id", nullable = false)
    private String uniquePosterId;

    @Column(name = "movie_price", nullable = false)
    private int price;

    @OneToMany(mappedBy = "reviewForMovie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews;

    @Column(name = "overall_rating", nullable = true)
    private Double rating;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "movie_viewer_mapping",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "viewer_id", referencedColumnName = "id")
    )
    private List<Viewer> viewers;

    @OneToOne(mappedBy = "belongsToMovie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Episode episode;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    public MovieResponse to(){
        return MovieResponse.builder().movieId(this.id).description(this.description)
                .genre(this.genre).posterURL(this.uniquePosterId)
                .name(this.name).price(this.price).rating(
                        this.rating != null ? this.rating : -1
                )
                .createdAt(this.createdAt.toString())
                .uniqueMovieId(this.uniquePosterId)
                .posterURL(StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_POSTER_PATH + this.uniquePosterId)
                .build();
    }
}
