package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Admin;
import com.Core_Service.model.User;
import com.Core_Service.model_request.AdminCreateRequest;
import com.Core_Service.model_request.PrivateMessageRequest;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.cache_repository.AdminServiceCacheRepository;
import com.Core_Service.repository.db_repository.AdminRepository;
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
@CacheConfig(cacheNames = "admin_service_cache", cacheManager = "customCacheManager")
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminServiceCacheRepository cacheRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public UserResponse createAdmin(AdminCreateRequest adminCreateRequest){
        UserCreateRequest userCreateRequest = adminCreateRequest.toUserCreateRequest();
        User user = userService.createUser(userCreateRequest);
        Admin admin = adminCreateRequest.to();
        admin.setUser(user);
        admin.setUniqueProfileId(Helper.generateUUID());
        adminRepository.save(admin);

        cacheRepository.clearCacheAdminList();

        notificationService.notifyUsers(PrivateMessageRequest.builder()
                .userIdList(Arrays.asList(user.getId()))
                .content("Your account has been creates Successfully!!! \n With username: " + user.getUsername())
                .build());

        return user.to(admin);
    }

    @Cacheable(
            key = "'admin::list::' + #pageRequest.getPageNumber() + '::' + #pageRequest.getPageSize()",
            cacheNames = "admin_list"
    )
    public List<UserResponse> findAllAdminDescOrderById(Pageable pageRequest) {
        return adminRepository.findAllAdminsDescOrderById(pageRequest).getContent()
                .stream().map(x -> x.getUser().to(x)).collect(Collectors.toList());
    }

    @Cacheable(key = "'USER::' + #userId")
    public UserResponse findById(Long userId) throws NoUserFoundException {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new NoUserFoundException("No Admin Found With Provided Id !!!"));
        return admin.getUser().to(admin);
    }

    public UserResponse currentAdminDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = user.getAdmin();
        return user.to(admin);
    }
}
