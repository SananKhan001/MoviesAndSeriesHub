package com.Core_Service.controller;

import com.Core_Service.model_request.PrivateMessageRequest;
import com.Core_Service.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "notifyAllUsers")
    public Boolean notifyAllUsers(@Argument String message) {
        return notificationService.notifyAllUsers(message);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "notifyUsers")
    public Boolean notifyUsers(@Argument @Valid PrivateMessageRequest message) {
        System.out.println(message.toString());
        return notificationService.notifyUsers(message);
    }

}
