package NotificationService.config_websocket;

import NotificationService.repository.redis_db_repo.ActiveUserRepository;
import NotificationService.service.WSservice;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@EnableCaching
public class WebSocketEventListener {
    @Autowired
    private WSservice wSservice;

    @Autowired
    private ActiveUserRepository activeUserRepository;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        Long activeUsers = activeUserRepository.getActiveUsersCount();
        activeUsers = activeUsers == 0 ? 0 : activeUsers - 1;
        activeUserRepository.updateActiveUserCount(activeUsers);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event){
        Boolean isFirstUser = activeUserRepository.isFirstUser();
        if(isFirstUser) {
            activeUserRepository.updateActiveUserCount(0L);
            activeUserRepository.isFirstUser(false);
        }

        Long activeUsers = activeUserRepository.getActiveUsersCount() + 1;
        activeUserRepository.updateActiveUserCount(activeUsers);
    }
}
