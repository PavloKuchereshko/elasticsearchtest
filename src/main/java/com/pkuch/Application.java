package com.pkuch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkuch.model.histogram.Histogram;
import com.pkuch.service.JestRestClient;
import io.searchbox.client.JestResult;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        JestRestClient jestRestClient = JestRestClient.getInstance();
        int rating = 4;

        JestResult reviewsWithRating = jestRestClient.getReviewsWithRating(rating);

        System.out.println("***********************************************************");
        System.out.println("Reviews with rating " + rating + ": "
                + reviewsWithRating.getJsonObject().getAsJsonObject("hits").get("total"));
        System.out.println("***********************************************************");

//        {
//            "took": 17,
//                "timed_out": false,
//                "_shards": {
//            "total": 1,
//                    "successful": 1,
//                    "failed": 0
//         },
//            "hits": {
//            "total": 1668,                    <--- here is this
//                    "max_score": 3.463488,
//                    "hits": [
//                          { ... }
//             }
//         }

        JestResult ratingHistogram = jestRestClient.getHistogramOfAllRatings();

        ObjectMapper mapper = new ObjectMapper();

        Histogram histogram = mapper.readValue(ratingHistogram.getJsonString(), Histogram.class);

        System.out.println("Histogram of all the ratings:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(histogram));
        System.out.println("***********************************************************");

//        {
//            "took": 14,
//                "timed_out": false,
//                "_shards": {
//            "total": 1,
//                    "successful": 1,
//                    "failed": 0
//        },
//            "hits": {
//            "total": 70075,
//                    "max_score": 0,
//                    "hits": []
//        },
//            "aggregations": {
//            "group_by_rating": {
//                "buckets": [
//                {
//                    "key": 5,
//                        "doc_count": 2160
//                },
//                {
//                    "key": 4,
//                        "doc_count": 1668
//                },
//                {
//                    "key": 3,
//                        "doc_count": 1077
//                },
//                {
//                    "key": 2,
//                        "doc_count": 719
//                },
//                {
//                    "key": 1,
//                        "doc_count": 722
//                }
//            ]
//            }
//        }
//        }
    }
}
