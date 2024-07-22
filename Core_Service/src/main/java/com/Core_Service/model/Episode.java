package com.Core_Service.model;

import com.Core_Service.model_response.EpisodeResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "episode_name", unique = true, nullable = false)
    private String episodeName;

    // uuid
    @Column(name = "episode_id", nullable = false)
    private String episodeId;

    // uuid
    @Column(name = "unique_poster_id", nullable = false)
    private String uniquePosterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "belongs_to_series")
    private Series belongsToSeries;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "belongs_to_movie")
    private Movie belongsToMovie;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    public EpisodeResponse to(){
        return EpisodeResponse.builder().episodeId(this.id)
                .episodeName(this.episodeName).idInStreamDB(this.episodeId)
                .posterURL(this.uniquePosterId).createdAt(this.createdAt.toString()).build();
    }
}
