package com.Core_Service.model;

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
@Table(name = "admins")
public class Admin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "admin_name", unique = true, nullable = false)
    private String name;

    @Column(name = "unique_profile_id", nullable = false)
    private String uniqueProfileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credentials")
    @JsonIgnore
    private User user;

    @CreationTimestamp
    @Column(name = "joined_on")
    private Date joinedOn;

}
