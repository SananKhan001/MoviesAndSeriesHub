package com.Core_Service.service;

import com.Core_Service.model.Admin;
import com.Core_Service.model.User;
import com.Core_Service.model_request.AdminCreateRequest;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.AdminRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
        adminRepository.save(admin);
        return user.to(admin);
    }
}
