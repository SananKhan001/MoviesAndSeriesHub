package com.Core_Service.model;

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
@Table(name = "viewers")
public class Viewer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "viewer_name", nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credentials")
    @JsonIgnore
    private User user;

    @Column(name = "total_purchased_amount", nullable = false)
    private Long totalPurchasedAmount;

    // uuid
    @Column(name = "unique_profile_id", nullable = false)
    private String uniqueProfileId;

    @ManyToMany(mappedBy = "viewers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Movie> purchasedMovies;

    @ManyToMany(mappedBy = "viewers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Series> purchasedSeries;

    @OneToMany(mappedBy = "viewer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviewList;

    @OneToMany(mappedBy = "viewer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactionList;

    @CreationTimestamp
    @Column(name = "joined_at")
    private Date joinedAt;
}
