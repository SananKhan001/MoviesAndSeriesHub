package NotificationService.service;

import NotificationService.model.GlobalNotification;
import NotificationService.model.PersonalNotification;
import NotificationService.model.User;
import NotificationService.repository.db_repo.GlobalNotificationRepository;
import NotificationService.repository.db_repo.PersonalNotificationRepository;
import NotificationService.repository.db_repo.UserRepository;
import NotificationService.response.ResponseMessage;
import org.commonDTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSservice {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PersonalNotificationRepository personalNotificationRepository;

    @Autowired
    private GlobalNotificationRepository globalNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMessage(String message){
        GlobalNotification notification = GlobalNotification.builder()
                .content(message).build();
        notification = globalNotificationRepository.save(notification);
        simpMessagingTemplate.convertAndSend("/topic/messages", ResponseMessage.builder()
                .content(notification.getContent())
                .date(notification.getCreationDate()).build());
    }

    public void sendUser(NotificationMessage message) {

        PersonalNotification personalNotification = PersonalNotification.builder()
                .content(message.getContent()).build();
        PersonalNotification finalPersonalNotification = personalNotificationRepository.save(personalNotification);

        message.getUserIdList()
                        .forEach(id -> {
                            User user = userRepository.findById(id).orElse(null);
                            if(user != null) {
                                personalNotificationRepository.updateNotificationRow(finalPersonalNotification.getId(), user.getId());
                                simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/messages", ResponseMessage.builder()
                                        .content(finalPersonalNotification.getContent())
                                        .date(finalPersonalNotification.getCreationTime()).build());
                            }
                        });

    }

    public void sendActiveUserCount(Long activeUserCount) {
        simpMessagingTemplate.convertAndSend("/topic/active", activeUserCount);
    }
}