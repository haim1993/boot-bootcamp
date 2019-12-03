package com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Singleton
@Path("api")
public class Resource {

    private LogsConfiguration logsConfiguration;
    private String seed;

    public static int VISITS_COUNTER = 1;
    public static int ID = 1;

    private RestHighLevelClient client;


    @Inject
    public Resource(LogsConfiguration logsConfiguration, RestHighLevelClient client) {
        this.logsConfiguration = logsConfiguration;
        this.client = client;

        // Generate unique random seed
        this.seed = UUID.randomUUID().toString();
    }


    @GET
    @Path("logs")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendLog() {
        String response =   "{ " +
                                "Seed: " + seed + ", " +
                                "Visits: " + (VISITS_COUNTER++ + ", " +
                                "LogMessage: " + logsConfiguration.getLogMessage() +
                            " }");

        Logger logger = LogManager.getLogger(Resource.class);
        logger.info(response);

        return response;
    }

    @POST
    @Path("index")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTrackInJSON(Map<String, String> jsonAsMap, @HeaderParam("user-agent") String userAgent) {
        IndexRequest request = new IndexRequest("posts", "_doc");
        request.id("" + ID++);

        // Adding the user agent
        jsonAsMap.put("User-Agent", userAgent);

        request.source(jsonAsMap);

        client.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println("Success Index");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Failure Index");
                e.printStackTrace();
            }
        });

        String result = "Success";
        return Response.status(201).entity(result).build();
    }

    @GET
    @Path("search")
    @Produces(MediaType.TEXT_PLAIN)
    public String search(@QueryParam("message") String message, @QueryParam("header") String header) {

        SearchRequest searchRequest = new SearchRequest("posts");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("message", message))
                .must(QueryBuilders.matchQuery("User-Agent", header));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            StringBuilder builder = new StringBuilder();
            for (SearchHit hit : hits) {
                builder.append(hit.getSourceAsString() + "\n");
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}