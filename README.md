# elasticsearchtest

After you run start-test-env.sh

1. Find reviews where rating == 4:
http://localhost:9200/&part-pants/_search
 in Postman with body:
```json
{
  "query": {
    "bool": {
      "must": [
        { "match" : {"type" : "review" } },
        { "match": { "rating" : 4 } }
      ]
    }
  }
}
```

2. Compute a histogram of all the ratings (using ES aggregates)
http://localhost:9200/&part-pants/_search
in Postman with body:
```json
{
  "size": 0,
  "aggs": {
    "group_by_rating" : {
      "histogram": {
        "field": "rating",
        "interval": "1",
        "order" : { "_key" : "desc" }
      }
    }
  }
}
```
