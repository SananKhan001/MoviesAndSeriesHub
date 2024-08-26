package NotificationService.service;

import NotificationService.model.GlobalNotification;
import NotificationService.model.PersonalNotification;
import NotificationService.repository.GlobalNotificationRepository;
import NotificationService.repository.PersonalNotificationRepository;
import NotificationService.request.PrivateMessageRequest;
import NotificationService.response.ResponseMessage;
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

    public void sendMessage(String message){
        GlobalNotification notification = GlobalNotification.builder()
                .content(message).build();
        notification = globalNotificationRepository.save(notification);
        simpMessagingTemplate.convertAndSend("/topic/messages", ResponseMessage.builder()
                .content(notification.getContent())
                .date(notification.getCreationDate()).build());
    }

    public void sendUser(PrivateMessageRequest messageRequest) {
//        System.out.println(message.getMessageContent());
//        simpMessagingTemplate.convertAndSendToUser(id,"/topic/private-messages",new ResponseMessage(message.getMessageContent()));
    }
}