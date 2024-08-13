package com.Search_Service.Search_Service.utils;


import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.List;
import java.util.function.Supplier;

public class ElasticSearchUtil {
    public static Supplier<Query> matchQuerySupplier(String field, String query){
        return () -> Query.of(q -> q.match(matchQuery(field, query)));
    }

    public static Supplier<Query> boolQuerySupplier(List<Query> filterQuery, List<Query> mustQuery) {
        return () -> Query.of(q -> q.bool(boolQuery(filterQuery, mustQuery)));
    }

    public static Supplier<Query> rangeQuerySupplier(String field, int gte, int lte) {
        return () -> Query.of(q -> q.range(rangeQuery(field, gte, lte)));
    }

    public static Supplier<Query> autoSuggestMatchQuerySupplier(String field, String query) {
        return () -> Query.of(q -> q.match(autoSuggestMatchQuery(field, query)));
    }

    public static MatchQuery matchQuery(String field, String query){
        return new MatchQuery.Builder().field(field).query(query).build();
    }

    public static RangeQuery rangeQuery(String field, int gte, int lte) {
        return new RangeQuery.Builder().field(field).gte(JsonData.of(gte)).lte(JsonData.of(lte)).build();
    }

    public static BoolQuery boolQuery(List<Query> filterQuery, List<Query> mustQuery){
        return new BoolQuery.Builder().filter(filterQuery).must(mustQuery).build();
    }

    public static MatchQuery autoSuggestMatchQuery(String field, String query) {
        return new MatchQuery.Builder().field(field).query(query)
                .analyzer("autocomplete_search").operator(Operator.Or).build();
    }
}
