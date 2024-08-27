package com.Core_Service.service;

import com.Core_Service.helpers.StreamServiceDetails;
import com.Core_Service.model_request.PrivateMessageRequest;
import org.commonDTO.MovieCreationMessage;
import org.commonDTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private StreamBridge streamBridge;

    public Boolean notifyAllUsers(String message) {

        /**                    --------------------------
         *  Message ======>>> | GlobalNotificationTopic |
         *                    --------------------------
         */
        streamBridge.send("GlobalNotificationTopic", message);

        return true;
    }

    public Boolean notifyUsers(PrivateMessageRequest privateMessageRequest) {
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .content(privateMessageRequest.getContent())
                .userIdList(privateMessageRequest.getUserIdList()).build();
        /**                                  ---------------------------
         *  PrivateMessageRequest ======>>> | PrivateNotificationTopic |
         *                                  ---------------------------
         */
        streamBridge.send("PrivateNotificationTopic", notificationMessage);

        return true;
    }
}
