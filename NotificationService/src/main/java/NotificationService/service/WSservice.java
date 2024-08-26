package NotificationService.service;

import NotificationService.model.GlobalNotification;
import NotificationService.model.PersonalNotification;
import NotificationService.model.User;
import NotificationService.repository.GlobalNotificationRepository;
import NotificationService.repository.PersonalNotificationRepository;
import NotificationService.repository.UserRepository;
import NotificationService.response.ResponseMessage;
import org.commonDTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

//    public void sendMessage(String message){
//        GlobalNotification notification = GlobalNotification.builder()
//                .content(message).build();
//        notification = globalNotificationRepository.save(notification);
//        simpMessagingTemplate.convertAndSend("/topic/messages", ResponseMessage.builder()
//                .content(notification.getContent())
//                .date(notification.getCreationDate()).build());
//    }

    public void sendUser(NotificationMessage message) {
        List<User> userList = userRepository.findAllById(message.getUserIdList());
        PersonalNotification personalNotification = PersonalNotification.builder()
                .content(message.getContent())
                .userList(userList).build();
        personalNotification = personalNotificationRepository.save(personalNotification);

        PersonalNotification finalPersonalNotification = personalNotification;
        message.getUserIdList()
                        .parallelStream()
                                .forEach(id -> {
                                    User user = userRepository.findById(id).get();
                                    System.out.println(user.getUsername());
                                    simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/messages", ResponseMessage.builder()
                                            .content(finalPersonalNotification.getContent())
                                            .date(finalPersonalNotification.getCreationTime()).build());
                                });

    }
}