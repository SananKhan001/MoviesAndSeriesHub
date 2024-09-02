package NotificationService.controller;

import NotificationService.repository.redis_db_repo.ActiveUserRepository;
import NotificationService.service.WSservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping
public class MessageController {

    @Autowired
    private WSservice wSservice;

    @MessageMapping("/unseen/notifications")
    public ResponseEntity unseenNotifications(Principal principal) {
        wSservice.showUnseenNotifications(principal);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/active/user/count")
    public ResponseEntity getActiveUserCount(Principal principal) {
        wSservice.getActiveUserCount(principal);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/unseen/notification/count")
    public ResponseEntity unseenNotificationCount(Principal principal) {
        wSservice.getUnseenNotificationCount(principal);
        return ResponseEntity.ok().build();
    }

}
