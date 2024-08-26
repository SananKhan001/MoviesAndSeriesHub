package NotificationService.config_websocket;

import NotificationService.config_jwt.JwtHelper;
import NotificationService.model.User;
import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Configuration
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Value("${jwt.header.key}")
    private String headerKey;

    @Value("${jwt.token.starts.with}")
    private String tokenStartsWith;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = UUID.randomUUID().toString();
        attributes.put("username", user.getUsername());
        return new UserPrincipal(user.getUsername());
    }
}