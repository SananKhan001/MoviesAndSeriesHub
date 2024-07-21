package com.Core_Service.model_response;

import lombok.Builder;

import java.util.Date;

@Builder
public class MovieResponse {
    private Long movieId;
    private String name;
    private String genre;
    private String description;
    private String posterURL;
    private int price;
    private int rating;
    private String createdAt;
}
