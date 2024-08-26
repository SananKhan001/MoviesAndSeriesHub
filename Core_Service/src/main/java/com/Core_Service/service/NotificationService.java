package com.Core_Service.service;

import com.Core_Service.helpers.StreamServiceDetails;
import org.commonDTO.MovieCreationMessage;
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
}
