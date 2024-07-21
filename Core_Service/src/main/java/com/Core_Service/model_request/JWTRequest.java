package com.Core_Service.model_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JWTRequest {

    @NotBlank(message = "gmail should not be blank")
    @NotNull(message = "gmail should not be null")
    @Email(message = "gmail format is wrong")
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@gmail\\.com$", message = "gmail format is wrong")
    private String username;

    @NotBlank(message = "password should not be blank")
    @NotNull(message = "password should not be null")
    private String password;

}
