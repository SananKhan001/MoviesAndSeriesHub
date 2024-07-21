package com.Core_Service.controller;

import com.Core_Service.model_request.MovieCreateRequest;
import com.Core_Service.model_response.MovieResponse;
import com.Core_Service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @MutationMapping(name = "addMovie")
    public MovieResponse addMovie(@Argument MovieCreateRequest movieCreateRequest) {
        return movieService.addMovie(movieCreateRequest);
    }

}
