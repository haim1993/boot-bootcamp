package jersey.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.LogsConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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
import java.util.concurrent.ExecutionException;

@Singleton
@Path("/")
public class Resource {

    // Generate unique random seed
    private static final String SEED = UUID.randomUUID().toString();
    private static final String INDEX = "posts";
    private static int VISITS_COUNTER = 1;
    private static final String TOPIC = "sample";

    private LogsConfiguration logsConfiguration;
    private final RestHighLevelClient elasticSearchClient;
    private final KafkaProducer<Integer, String> producer;
    private static int PRODUCER_MESSAGE_COUNT = 1;


    @Inject
    public Resource(LogsConfiguration logsConfiguration, RestHighLevelClient elasticSearchClient, KafkaProducer<Integer, String> producer) {
        this.logsConfiguration = logsConfiguration;
        this.elasticSearchClient = elasticSearchClient;
        this.producer = producer;
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
    public Response index(RequestIndexMessage requestIndexMessage, @HeaderParam("user-agent") String userAgent) {
        Map<String, Object> docToIndex = buildDocToIndex(requestIndexMessage, userAgent);
        if (docToIndex == null) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("The given object is null\n").build();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(docToIndex);

            producer.send(new ProducerRecord<>(TOPIC, PRODUCER_MESSAGE_COUNT++, msg)).get();
            producer.flush();
            System.out.println("Sent message: " + msg);
            return Response.status(HttpURLConnection.HTTP_OK).entity("Success\n").build();
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Producer failed to communicate with kafka\n").build();
        }
    }

    @GET
    @Path("/api/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("message") String message, @QueryParam("header") String header) {
        Tuple<String, String> messagePair    = new Tuple<>("message", message);
        Tuple<String, String> agentPair      = new Tuple<>("User-Agent", header);

        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.source(getMatchQuerySourceBuilder(messagePair, agentPair));

        try {
            SearchResponse searchResponse = elasticSearchClient.search( searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();

            StringBuilder builder = new StringBuilder();
            for (SearchHit hit : hits) {
                builder.append(hit.getSourceAsString() + "\n");
            }
            return Response.status(HttpURLConnection.HTTP_OK).entity(builder.toString()).build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Failed\n").build();
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