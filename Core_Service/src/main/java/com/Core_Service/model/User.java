package com.Core_Service.model;

import com.Core_Service.helpers.StreamServiceDetails;
import com.Core_Service.model_response.UserResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "user_details")
public class User implements UserDetails, Serializable {

    private static final String AUTHORITY_DELIMETER = ":";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "gmail",unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "authority", nullable = false)
    private String authority;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Admin admin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Viewer viewer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] authorities = this.authority.split(AUTHORITY_DELIMETER);
        return Arrays.stream(authorities)
                .map(x -> new SimpleGrantedAuthority(x))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO:: To Be Implemented
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO:: To Be Implemented
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO:: To Be Implemented
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO:: To Be Implemented
        return true;
    }

    public UserResponse to(Admin admin){
        return UserResponse.builder().userId(this.id).name(admin.getName())
                .username(this.username).authority(this.authority)
                .uniqueProfileId(admin.getUniqueProfileId())
                .profileURL(StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_PROFILE_PATH + admin.getUniqueProfileId()).build();
    }

    public UserResponse to(Viewer viewer){
        return UserResponse.builder().userId(this.id).name(viewer.getName())
                .username(this.username).authority(this.authority)
                .profileURL(StreamServiceDetails.STREAM_SERVER_URL + StreamServiceDetails.MEDIA_URI_GET_PROFILE_PATH + viewer.getUniqueProfileId())
                .uniqueProfileId(viewer.getUniqueProfileId()).build();
    }
}
