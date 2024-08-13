package com.Search_Service.Search_Service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.Search_Service.Search_Service.entity.Movie;
import com.Search_Service.Search_Service.enums.IndexReference;
import com.Search_Service.Search_Service.request.SearchRequest;
import com.Search_Service.Search_Service.utils.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<?> search(SearchRequest searchRequest) throws IOException {

        String index = searchRequest.getIndex().equals(IndexReference.MOVIE_INDEX) ? "movies" : "series";
        Supplier<Query> mustQuerySupplier = ElasticSearchUtil.matchQuerySupplier("searchableDescription", searchRequest.getDescription());

        if(searchRequest.getStartingPrice() != null && searchRequest.getEndPrice() != null &&
                searchRequest.getMinRating() != null && searchRequest.getMaxRating() != null) {
            // Searching by combining every thing
            Query filterByRatingQuery = ElasticSearchUtil.rangeQuerySupplier("rating", searchRequest.getMinRating(), searchRequest.getMaxRating()).get();
            Query filterByPriceQuery = ElasticSearchUtil.rangeQuerySupplier("price", searchRequest.getStartingPrice(), searchRequest.getEndPrice()).get();
            Supplier<Query> boolQuery = ElasticSearchUtil.boolQuerySupplier(Arrays.asList(filterByPriceQuery, filterByRatingQuery), Collections.singletonList(mustQuerySupplier.get()));

            return search(searchRequest, boolQuery, index);
        } else if ((searchRequest.getStartingPrice() == null || searchRequest.getEndPrice() == null) &&
                (searchRequest.getMinRating() != null && searchRequest.getMaxRating() != null)) {
            // Search by combining description and rating
            Query filterByRatingQuery = ElasticSearchUtil.rangeQuerySupplier("rating", searchRequest.getMinRating(), searchRequest.getMaxRating()).get();
            Supplier<Query> boolQuery = ElasticSearchUtil.boolQuerySupplier(Collections.singletonList(filterByRatingQuery), Collections.singletonList(mustQuerySupplier.get()));

            return search(searchRequest, boolQuery, index);
        } else if (searchRequest.getStartingPrice() != null && searchRequest.getEndPrice() != null){
            // Search by combining description and pricing
            Query filterByPriceQuery = ElasticSearchUtil.rangeQuerySupplier("price", searchRequest.getStartingPrice(), searchRequest.getEndPrice()).get();
            Supplier<Query> boolQuery = ElasticSearchUtil.boolQuerySupplier(Collections.singletonList(filterByPriceQuery), Collections.singletonList(mustQuerySupplier.get()));

            return search(searchRequest, boolQuery, index);
        } else {
            // Search only by Description
            return search(searchRequest, mustQuerySupplier, index);
        }
    }

    public List<String> autoSuggest(String partialName, IndexReference indexReference) throws IOException {
        Supplier<Query> querySupplier = ElasticSearchUtil.autoSuggestMatchQuerySupplier("name", partialName);
        String index = indexReference.equals(IndexReference.MOVIE_INDEX) ? "movies" : "series";

        return elasticsearchClient.search(s -> s.index(index)
                        .from(0)
                        .size(10)
                        .query(querySupplier.get()),Movie.class)
                .hits().hits().stream()
                .map(Hit::source).map(obj -> obj.getName()).collect(Collectors.toList());

    }

    private List<?> search(SearchRequest request, Supplier<Query> supplier, String index) throws IOException {
        return elasticsearchClient.search(s -> s.index(index)
                        .from(request.getPage() * request.getSize())
                        .size(request.getSize())
                        .query(supplier.get()),Movie.class)
                .hits().hits().stream()
                .map(Hit::source).collect(Collectors.toList());
    }
}
