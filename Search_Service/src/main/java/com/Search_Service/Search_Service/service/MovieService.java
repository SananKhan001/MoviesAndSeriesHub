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

    @Autowired
    private ElasticsearchClient elasticsearchClient;

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

    public List<Movie> search(SearchRequest searchRequest) throws IOException {

        Supplier<Query> mustQuerySupplier = ElasticSearchUtil.matchQuerySupplier("searchableDescription", searchRequest.getMovieDescription());

        if(searchRequest.getStartingPrice() != null && searchRequest.getEndPrice() != null &&
            searchRequest.getMinRating() != null && searchRequest.getMaxRating() != null) {
            // Searching by combining every thing
            Query filterByRatingQuery = ElasticSearchUtil.rangeQuerySupplier("rating", searchRequest.getMinRating(), searchRequest.getMaxRating()).get();
            Query filterByPriceQuery = ElasticSearchUtil.rangeQuerySupplier("price", searchRequest.getStartingPrice(), searchRequest.getEndPrice()).get();
            Supplier<Query> boolQuery = ElasticSearchUtil.boolQuerySupplier(Arrays.asList(filterByPriceQuery, filterByRatingQuery), Arrays.asList(mustQuerySupplier.get()));

            return search(searchRequest, boolQuery);
        } else if ((searchRequest.getStartingPrice() == null || searchRequest.getEndPrice() == null) &&
                    (searchRequest.getMinRating() != null && searchRequest.getMaxRating() != null)) {
            // Search by combining description and rating
            Query filterByRatingQuery = ElasticSearchUtil.rangeQuerySupplier("rating", searchRequest.getMinRating(), searchRequest.getMaxRating()).get();
            Supplier<Query> boolQuery = ElasticSearchUtil.boolQuerySupplier(Arrays.asList(filterByRatingQuery), Arrays.asList(mustQuerySupplier.get()));

            return search(searchRequest, boolQuery);
        } else if (searchRequest.getStartingPrice() != null && searchRequest.getEndPrice() != null){
            // Search by combining description and pricing
            Query filterByPriceQuery = ElasticSearchUtil.rangeQuerySupplier("price", searchRequest.getStartingPrice(), searchRequest.getEndPrice()).get();
            Supplier<Query> boolQuery = ElasticSearchUtil.boolQuerySupplier(Arrays.asList(filterByPriceQuery), Arrays.asList(mustQuerySupplier.get()));

            return search(searchRequest, boolQuery);
        } else {
            // Search only by Description
            return search(searchRequest, mustQuerySupplier);
        }
    }

    private List<Movie> search(SearchRequest request, Supplier<Query> supplier) throws IOException {
        return elasticsearchClient.search(s -> s.index("movies")
                .from(request.getPage() * request.getSize())
                .size(request.getSize())
                .query(supplier.get()),Movie.class)
                .hits().hits().stream()
                .map(hit -> hit.source()).collect(Collectors.toList());
    }
}
