package com.Stream_Service.service;

import com.Stream_Service.models.UserMovieMapping;
import com.Stream_Service.repository.UserMovieMappingRepository;
import org.commonDTO.MovieBuyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MovieService {

    @Autowired
    private UserMovieMappingRepository userMovieMappingRepository;

    public Mono<Void> createUserMovieMapping(MovieBuyMessage movieBuyMessage) {
        UserMovieMapping userMovieMapping = UserMovieMapping.builder()
                .movieId(movieBuyMessage.getMovieId())
                .userId(movieBuyMessage.getUserId())
                .isNew(movieBuyMessage.isNew()).build();

        userMovieMappingRepository.save(userMovieMapping).subscribe();
        return Mono.empty();
    }

    public Mono<Void> deleteUserMovieMapping(Long movieId) {
        userMovieMappingRepository.deleteByMovieId(movieId).subscribe();
        return Mono.empty();
    }
}
