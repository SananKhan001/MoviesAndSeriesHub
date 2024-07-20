package com.Core_Service.controller;

import com.Core_Service.model_response.UserResponse;
import com.Core_Service.service.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;


@RestController
@PreAuthorize("hasAuthority('VIEWER')")
public class ViewerController {

    @Autowired
    private ViewerService viewerService;

    @QueryMapping(name = "currentViewerDetails")
    public UserResponse currentViewerDetails(){
        return viewerService.currentViewerDetails();
    }

}
