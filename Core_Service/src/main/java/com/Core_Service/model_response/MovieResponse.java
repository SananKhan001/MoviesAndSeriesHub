package com.Core_Service.model_response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class MovieResponse implements Serializable {
    private Long movieId;
    private String name;
    private String genre;
    private String description;
    private String uniqueMovieId;
    private String posterURL;
    private int price;
    private double rating;
    private String createdAt;
}
