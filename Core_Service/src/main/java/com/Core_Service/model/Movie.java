package com.Core_Service.model;

import com.Core_Service.enums.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "movie_name", nullable = false, unique = true)
    private String name;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "movie_description", nullable = false)
    private String description;

    // uuid
    @Column(name = "unique_poster_id", nullable = false)
    private String uniquePosterId;

    @Column(name = "movie_price", nullable = false)
    private int price;

    @OneToMany(mappedBy = "reviewForMovie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    @Column(name = "overall_rating", nullable = true)
    private int rating;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "movie-viewer-mapping",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "viewer_id", referencedColumnName = "id")
    )
    private List<Viewer> viewers;

    @OneToOne(mappedBy = "belongsToMovie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Episode episode;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
}
