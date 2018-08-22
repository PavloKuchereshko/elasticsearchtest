package com.pkuch.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.HistogramAggregation;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

public class JestRestClient {
    private static JestRestClient jestRestClient = new JestRestClient();
    private final JestClientFactory jestClientFactory;

    public JestRestClient() {
        jestClientFactory = new JestClientFactory();
        jestClientFactory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://localhost:9200")
                .multiThreaded(true)
                .build());
    }

    public JestResult getReviewsWithRating(int rating) throws IOException {
        JestClient jestClient = jestClientFactory.getObject();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(new MatchQueryBuilder("type", "review"))
                .must(new MatchQueryBuilder("rating", rating)));

        Search search = new Search.Builder(searchSourceBuilder.toString()).build();

        SearchResult searchResult = jestClient.execute(search);
        jestClient.shutdownClient();

        return searchResult;
    }

    public JestResult getHistogramOfAllRatings() throws IOException {
        JestClient jestClient = jestClientFactory.getObject();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(AggregationBuilders.histogram("hist")
                .field("rating").interval(1)
                .order(Histogram.Order.KEY_DESC));

        Search search = new Search.Builder(searchSourceBuilder.toString()).build();

        SearchResult searchResult = jestClient.execute(search);
        jestClient.shutdownClient();

        return searchResult;
    }

    public static JestRestClient getInstance() {
        return jestRestClient;
    }
}
