package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.model_request.ViewerCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.ViewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.View;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class ViewerService {

    @Autowired
    private ViewerRepository viewerRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public UserResponse createViewer(ViewerCreateRequest viewerCreateRequest){
        UserCreateRequest userCreateRequest = viewerCreateRequest.toUserCreateRequest();
        User user = userService.createUser(userCreateRequest);
        Viewer viewer = viewerCreateRequest.to();
        viewer.setUser(user);
        viewer.setUniqueProfileId(Helper.generateUUID());
        viewerRepository.save(viewer);
        return user.to(viewer);
    }

    public List<UserResponse> findAllViewerDescOrderById(Pageable pageRequest) {
        return viewerRepository.findAllViewerDescOrderById(pageRequest).getContent()
                .stream().map(x -> x.getUser().to(x)).collect(Collectors.toList());
    }

    public UserResponse findById(Long userId) throws NoUserFoundException {
        Viewer viewer = viewerRepository.findById(userId)
                        .orElseThrow(() -> new NoUserFoundException("No Viewer Found With Provided Id !!!"));
        return viewer.getUser().to(viewer);
    }

    public UserResponse currentViewerDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Viewer viewer = user.getViewer();
        return user.to(viewer);
    }
}