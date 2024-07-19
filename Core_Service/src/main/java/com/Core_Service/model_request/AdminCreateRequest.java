package com.Core_Service.model_request;

import com.Core_Service.model.Admin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminCreateRequest {
    @NotNull(message = "username should not be null")
    @NotBlank(message = "username should not be blank")
    private String name;

    @NotBlank(message = "gmail should not be blank")
    @NotNull(message = "gmail should not be null")
    @Email(message = "gmail format is wrong")
    private String username;

    @NotBlank(message = "password should not be blank")
    @NotNull(message = "password should not be null")
    private String password;

    public Admin to(){
        return Admin.builder().name(this.name).build();
    }

    public UserCreateRequest toUserCreateRequest(){
        return UserCreateRequest.builder().username(this.username)
                .password(this.password).admin(to()).build();
    }
}
