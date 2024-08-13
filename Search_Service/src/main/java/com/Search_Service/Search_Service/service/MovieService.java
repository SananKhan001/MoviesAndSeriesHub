package com.Search_Service.Search_Service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.Search_Service.Search_Service.entity.Movie;
import com.Search_Service.Search_Service.repository.MovieRepository;
import com.Search_Service.Search_Service.request.SearchRequest;
import com.Search_Service.Search_Service.utils.ElasticSearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.commonDTO.MovieCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
