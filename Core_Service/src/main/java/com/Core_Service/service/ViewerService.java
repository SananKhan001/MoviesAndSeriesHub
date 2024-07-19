package com.Core_Service.service;

import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.model_request.ViewerCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.ViewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class ViewerService {

    @Autowired
    private ViewerRepository viewerRepository;

    @Autowired
    private UserService userService;

    public UserResponse createViewer(ViewerCreateRequest viewerCreateRequest){
        UserCreateRequest userCreateRequest = viewerCreateRequest.toUserCreateRequest();
        User user = userService.createUser(userCreateRequest);
        Viewer viewer = viewerCreateRequest.to();
        viewer.setUser(user);
        viewerRepository.save(viewer);
        return user.to(viewer);
    }

}
