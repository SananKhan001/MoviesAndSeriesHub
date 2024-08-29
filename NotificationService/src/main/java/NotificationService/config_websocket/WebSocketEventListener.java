package NotificationService.config_websocket;

import NotificationService.dto.NewNotification;
import NotificationService.dto.UserStatusMessage;
import NotificationService.enums.Status;
import NotificationService.model.User;
import NotificationService.repository.redis_db_repo.ActiveUserRepository;
import NotificationService.response.ResponseMessage;
import NotificationService.service.UserService;
import NotificationService.service.WSservice;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Configuration
@EnableCaching
public class WebSocketEventListener {
    @Autowired
    private UserService userService;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ActiveUserRepository activeUserRepository;

    private void updateUserStatus(String username, Status status) {
        User user = (User) userService.loadUserByUsername(username);
        UserStatusMessage userStatusMessage = UserStatusMessage.builder()
                .userId(user.getId()).status(status).username(username).build();

        streamBridge.send("UserStatusMessageTopic", userStatusMessage);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        updateUserStatus(event.getUser().getName(), Status.INACTIVE);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event){
        updateUserStatus(event.getUser().getName(), Status.ACTIVE);
    }

}
