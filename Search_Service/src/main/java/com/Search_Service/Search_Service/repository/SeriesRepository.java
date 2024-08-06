package com.Search_Service.Search_Service.repository;

import com.Search_Service.Search_Service.entity.Series;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends ElasticsearchRepository<Series, Long> {
}
