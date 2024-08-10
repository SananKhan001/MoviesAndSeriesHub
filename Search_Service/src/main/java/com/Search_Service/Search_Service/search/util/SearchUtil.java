package com.Search_Service.Search_Service.search.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.Search_Service.Search_Service.search.SearchRequestDTO;
import lombok.Builder;

import java.util.function.Supplier;

public final class SearchUtil {
    private SearchUtil() {}

    public static Supplier<Query> matchQuerySupplier(String field, String query){
        return () -> Query.of(q -> q.match(matchQuery(field, query)));
    }

    public static MatchQuery matchQuery(String field, String query){
        return new MatchQuery.Builder().field(field).query(query).build();
    }
}
