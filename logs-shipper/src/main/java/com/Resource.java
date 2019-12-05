package com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.collect.Tuple;
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
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
@Path("/")
public class Resource {

    // Generate unique random seed
    private static final String SEED = UUID.randomUUID().toString();
    private static final String INDEX = "posts";
    private static int VISITS_COUNTER = 1;

    private RestHighLevelClient elasticsearchClient;
    private LogsConfiguration logsConfiguration;


    @Inject
    public Resource(LogsConfiguration logsConfiguration, RestHighLevelClient elasticsearchClient) {
        this.logsConfiguration = logsConfiguration;
        this.elasticsearchClient = elasticsearchClient;
    }


    @GET
    @Path("logs")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendLog() {
        String response =   "{ " +
                                "Seed: " + SEED + ", " +
                                "Visits: " + (VISITS_COUNTER++ + ", " +
                                "LogMessage: " + logsConfiguration.getLogMessage() +
                            " }");

        Logger logger = LogManager.getLogger(Resource.class);
        logger.info(response);

        return response;
    }

    @POST
    @Path("/api/index")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTrackInJSON(RequestIndexMessage requestIndexMessage, @HeaderParam("user-agent") String userAgent) {
        Map<String, Object> docToIndex = buildDocToIndex(requestIndexMessage, userAgent);
        if (docToIndex == null) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("The given object is null\n").build();
        }

        IndexRequest request = new IndexRequest(INDEX, "_doc");
        request.source(docToIndex);

        return buildResponse(request);
    }

    @GET
    @Path("/api/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("message") String message, @QueryParam("header") String header) {
        Tuple<String, String> messagePair    = new Tuple<>("message", message);
        Tuple<String, String> agentPair      = new Tuple<>("User-Agent", header);

        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.source(getMatchQuerySourceBuilder(messagePair, agentPair));

        return buildResponse(searchRequest);
    }


    /**
     * We construct a json message document along with all needed params, and return
     * a Map type object of it.
     *
     * @param requestIndexMessage
     * @param userAgent
     * @return
     */
    public Map<String, Object> buildDocToIndex(RequestIndexMessage requestIndexMessage, String userAgent) {
        String msg = requestIndexMessage.getMessage();
        if (msg == null) return null;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("message", msg);
        jsonAsMap.put("User-Agent", userAgent);
        return jsonAsMap;
    }

    /**
     * Given a Generic type E, we analyze its type using the 'instanceof' to build a corresponding response.
     * Supported requests types are IndexRequest and SearchRequest.
     *
     * @param requestObject - the generic type
     * @param <E>
     * @return
     */
    public < E > Response buildResponse(E requestObject) {
        try {
            if (requestObject instanceof IndexRequest) {
                IndexResponse indexResponse = elasticsearchClient.index((IndexRequest) requestObject, RequestOptions.DEFAULT);
                return Response.status(HttpURLConnection.HTTP_OK).entity("Success\n").build();
            }
            else if (requestObject instanceof SearchRequest) {
                SearchResponse searchResponse = elasticsearchClient.search((SearchRequest) requestObject, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();

                StringBuilder builder = new StringBuilder();
                for (SearchHit hit : hits) {
                    builder.append(hit.getSourceAsString() + "\n");
                }
                return Response.status(HttpURLConnection.HTTP_OK).entity(builder.toString()).build();
            }
            else {
                return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Unrecognised object\n").build();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Failed\n").build();
    }

    /**
     * Given unknown args of Tuple's, containing key pairs of arguments,
     * we filter through the results that suffice all matched conditions.
     *
     * We construct a SearchSourceBuilder and return an instance of it, with the
     * filters that we calcuated.
     *
     * @param args
     * @return
     */
    public SearchSourceBuilder getMatchQuerySourceBuilder(Tuple<String,String>... args) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for(Tuple<String,String> p : args) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(p.v1(), p.v2()));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }

}

/**
 * Custom request index message
 */
class RequestIndexMessage {
    private String message;
    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }
}