package com.Core_Service.model_response;

import lombok.Builder;

@Builder
public class ReviewResponse {
    private Long id;
    private Long viewerId;
    private String comment;
    private int rating;
}
