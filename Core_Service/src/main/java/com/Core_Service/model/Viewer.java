package com.Core_Service.model;

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
@Table(name = "viewers")
public class Viewer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "viewer_name", nullable = false, unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_credentials")
    @JsonIgnore
    private User user;

    // uuid
    @Column(name = "unique_profile_id", nullable = false)
    private String uniqueProfileId;

    @ManyToMany(mappedBy = "viewers", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Movie> purchasedMovies;

    @ManyToMany(mappedBy = "viewers", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Series> purchasedSeries;

    @OneToMany(mappedBy = "viewer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviewList;

    @CreationTimestamp
    @Column(name = "joined_at")
    private Date joinedAt;
}
