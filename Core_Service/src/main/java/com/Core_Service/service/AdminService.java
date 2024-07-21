package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Admin;
import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_request.AdminCreateRequest;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.AdminRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public UserResponse createAdmin(AdminCreateRequest adminCreateRequest){
        UserCreateRequest userCreateRequest = adminCreateRequest.toUserCreateRequest();
        User user = userService.createUser(userCreateRequest);
        Admin admin = adminCreateRequest.to();
        admin.setUser(user);
        admin.setUniqueProfileId(Helper.generateUUID());
        adminRepository.save(admin);
        return user.to(admin);
    }

    public List<UserResponse> findAllViewerDescOrderById(Pageable pageRequest) {
        return adminRepository.findAllAdminsDescOrderById(pageRequest).getContent()
                .stream().map(x -> x.getUser().to(x)).collect(Collectors.toList());
    }

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
