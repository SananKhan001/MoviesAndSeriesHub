package com.Core_Service.model_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse implements Serializable {
    private Long userId;
    private String name;
    private String username;
    private String uniqueProfileId;
    private String profileURL;
    private String authority;
}
