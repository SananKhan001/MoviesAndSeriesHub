package com.Core_Service.model_response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class JWTResponse implements Serializable {
    private String jwtToken;
}
