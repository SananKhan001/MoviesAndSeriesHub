package NotificationService.service;

import NotificationService.dto.NewNotification;
import NotificationService.model.GlobalNotification;
import NotificationService.model.PersonalNotification;
import NotificationService.model.User;
import NotificationService.repository.db_repo.GlobalNotificationRepository;
import NotificationService.repository.db_repo.PersonalNotificationRepository;
import NotificationService.repository.db_repo.UserRepository;
import NotificationService.repository.redis_db_repo.ActiveUserRepository;
import NotificationService.response.ResponseMessage;
import org.commonDTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private ActiveUserRepository activeUserRepository;

    @Autowired
    private EmailService emailService;

    @Value("${mail.notification.subject}")
    private String emailSubject;

    @Value("${mail.notification.greeting}")
    private String emailGreeting;

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

        ResponseMessage responseMessage = ResponseMessage.builder()
                .content(finalPersonalNotification.getContent())
                .date(finalPersonalNotification.getCreationTime()).build();

        message.getUserIdList()
                        .forEach(id -> {
                            User user = userRepository.findById(id).orElse(null);
                            if(user != null) {

                                if(!activeUserRepository.getActiveUsersSet().contains(user.getId())) {
                                    activeUserRepository.updateUserNotificationCount(user.getUsername(),
                                            activeUserRepository.getUserNotificationCount(user.getUsername()) + 1);

                                    List<ResponseMessage> messageList = activeUserRepository.getUnseenNotifications(user.getUsername());
                                    messageList.add(responseMessage);
                                    activeUserRepository.setUnseenNotifications(user.getUsername(), messageList);
                                }

                                simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/messages", responseMessage);
                                emailService.sendEmail(emailSubject, emailGreeting, responseMessage.getContent(), user.getUsername());
                            }
                        });

    }

    public void sendActiveUserCount(Long activeUserCount) {
        simpMessagingTemplate.convertAndSend("/topic/active", activeUserCount);
    }

    public void sendNewNotificationCount(NewNotification newNotification) {
        simpMessagingTemplate.convertAndSendToUser(newNotification.getUsername(), "/topic/notification/count", newNotification.getNewNotificationCount());
    }

    public void showUnseenNotifications(Principal principal) {
        String username = principal.getName();
        List<ResponseMessage> messageList = activeUserRepository.getUnseenNotifications(username);
        activeUserRepository.setUnseenNotifications(username, new ArrayList<>());
        activeUserRepository.updateUserNotificationCount(username, 0);

        messageList.forEach(responseMessage -> {
            simpMessagingTemplate.convertAndSendToUser(username,
                    "/topic/unseen/messages", responseMessage);
        });

        sendNewNotificationCount(new NewNotification(0, username));
    }

    public void getActiveUserCount(Principal principal) {
        String username = principal.getName();
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/active", activeUserRepository.getActiveUsersSet().size());
    }

    public void getUnseenNotificationCount(Principal principal) {
        String username = principal.getName();
        sendNewNotificationCount(new NewNotification(activeUserRepository
                .getUserNotificationCount(username), username));
    }
}