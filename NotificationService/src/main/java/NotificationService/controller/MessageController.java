package NotificationService.controller;

import NotificationService.config_websocket.WSservice;
import NotificationService.model.GlobalNotification;
import NotificationService.request.GlobalMessageRequest;
import NotificationService.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {

    @Autowired
    private WSservice wSservice;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(@RequestBody ResponseMessage responseMessage){
        return responseMessage;
    }

    @PostMapping("/sendToAll")
    public void sendToAll(@RequestBody GlobalMessageRequest message){
        wSservice.sendMessage(message.getContent());
    }

}
