package com.Search_Service.Search_Service.repository;

import com.Search_Service.Search_Service.entity.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends ElasticsearchRepository<Movie, Long> {
}
