package com.Core_Service.service;

import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Movie;
import com.Core_Service.model_request.MovieCreateRequest;
import com.Core_Service.model_response.MovieResponse;
import com.Core_Service.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieResponse addMovie(MovieCreateRequest movieCreateRequest){
        Movie movie = Movie.builder().name(movieCreateRequest.getName())
                .genre(movieCreateRequest.getGenre().toString()).description(movieCreateRequest.getDescription())
                .uniquePosterId(Helper.generateUUID()).price(movieCreateRequest.getPrice())
                .build();
        return movieRepository.save(movie).to();
    }

}
