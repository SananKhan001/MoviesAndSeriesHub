package NotificationService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    private Long id;

    @Column(name = "gmail",unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "authority", nullable = false)
    private String authority;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "personal_notification_mapping",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id")
    )
    private List<PersonalNotification> personalNotifications;

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
}
