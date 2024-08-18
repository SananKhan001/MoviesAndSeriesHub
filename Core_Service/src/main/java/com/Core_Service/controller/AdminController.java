package com.Core_Service.controller;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.service.AdminService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @QueryMapping(name = "allAdminsInReverse")
    public List<UserResponse> getAllAdminsReverseOrder(@Argument
                                                        @Min(value = 0, message = "page can't go negative")
                                                        int page,
                                                        @Argument
                                                        @Min(value = 1, message = "page size should not be less than 1")
                                                        int size){
        Pageable pageRequest = PageRequest.of(page, size);
        return adminService.findAllAdminDescOrderById(pageRequest);
    }

    @QueryMapping(name = "findAdminsByUserId")
    public UserResponse findAdminsByUserId(    @Argument
                                               @NotNull(message = "userId should not be negative")
                                               Long userId) throws NoUserFoundException {
        return adminService.findById(userId);
    }

    @QueryMapping(name = "currentAdminDetails")
    public UserResponse currentAdminDetails(){
        return adminService.currentAdminDetails();
    }

}
