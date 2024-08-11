package com.Search_Service.Search_Service.repository;

import com.Search_Service.Search_Service.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends ElasticsearchRepository<Movie, Long> {
//    @Query("""
//            {
//                "match": {
//                    "searchableDescription": {
//                        "query": "?0"
//                    }
//                }
//            }
//            """)
//    List<Movie> searchByMovieDescription(String query, Pageable pageable);
}
