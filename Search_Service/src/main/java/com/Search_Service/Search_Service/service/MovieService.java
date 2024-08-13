package com.Search_Service.Search_Service.service;

import com.Search_Service.Search_Service.entity.Movie;
import com.Search_Service.Search_Service.repository.MovieRepository;
import org.commonDTO.MovieCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public void save(MovieCreationMessage movieCreationMessage){
        Movie movie = Movie.builder()
                .id(movieCreationMessage.getId()).name(movieCreationMessage.getName())
                .genre(movieCreationMessage.getGenre())
                .description(movieCreationMessage.getDescription())
                .searchableDescription(movieCreationMessage.getSearchableDescription())
                .posterURL(movieCreationMessage.getPosterURL())
                .price(movieCreationMessage.getPrice()).rating(movieCreationMessage.getRating())
                .createdAt(movieCreationMessage.getCreatedAt()).build();
        movieRepository.save(movie);
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    public Iterable<Movie> findAll() {
        return movieRepository.findAll();
    }
}
