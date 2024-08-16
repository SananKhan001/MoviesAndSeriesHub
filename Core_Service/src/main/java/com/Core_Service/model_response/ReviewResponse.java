package com.Core_Service.model_response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class ReviewResponse implements Serializable {
    private Long id;
    private Long viewerId;
    private String comment;
    private int rating;
}
