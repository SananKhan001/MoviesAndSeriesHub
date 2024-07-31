package com.Stream_Service.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_details")
public class User implements UserDetails, Persistable<Long>{
    private static final String AUTHORITY_DELIMETER = ":";

    @Id
    @Column("id")
    private Long id;

    @Column("gmail")
    private String username;

    @Column("password")
    private String password;

    @Column("authority")
    private String authority;

    @Transient
    private boolean isNew;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] authorities = this.authority.split(AUTHORITY_DELIMETER);
        return Arrays.stream(authorities)
                .map(x -> new SimpleGrantedAuthority(x))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
