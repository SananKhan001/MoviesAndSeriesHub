package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_request.PrivateMessageRequest;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.model_request.ViewerCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.cache_repository.ViewerServiceCacheRepository;
import com.Core_Service.repository.db_repository.ViewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@EnableCaching
@CacheConfig(cacheNames = "viewer_service_cache", cacheManager = "customCacheManager")
public class ViewerService {

    @Autowired
    private ViewerRepository viewerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ViewerServiceCacheRepository cacheRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public UserResponse createViewer(ViewerCreateRequest viewerCreateRequest){
        UserCreateRequest userCreateRequest = viewerCreateRequest.toUserCreateRequest();
        User user = userService.createUser(userCreateRequest);
        Viewer viewer = viewerCreateRequest.to();
        viewer.setUser(user);
        viewer.setUniqueProfileId(Helper.generateUUID());
        viewer.setTotalPurchasedAmount(0L);
        viewerRepository.save(viewer);

        cacheRepository.clearCacheViewerList();

        notificationService.notifyUsers(PrivateMessageRequest.builder()
                .userIdList(Arrays.asList(user.getId()))
                .content("Your account has been creates Successfully!!! \n With username: " + user.getUsername())
                .build());

        return user.to(viewer);
    }

    @Cacheable(
            key = "'viewer::list::' + #pageRequest.getPageNumber() + '::' + #pageRequest.getPageSize()",
            cacheNames = "viewer_list"
    )
    public List<UserResponse> findAllViewerDescOrderById(Pageable pageRequest) {
        return viewerRepository.findAllViewerDescOrderById(pageRequest).getContent()
                .stream().map(x -> x.getUser().to(x)).collect(Collectors.toList());
    }

    @Cacheable(key = "'USER::' + #userId")
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