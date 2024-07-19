package com.Core_Service.model_request;

import com.Core_Service.model.Admin;
import com.Core_Service.model.Viewer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    private String username;
    private String password;

    private Admin admin;
    private Viewer viewer;

}
