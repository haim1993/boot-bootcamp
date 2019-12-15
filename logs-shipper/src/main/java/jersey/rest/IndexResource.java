package jersey.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class IndexResource {

    private static final String TOPIC = "sample";

    private final KafkaProducer<Integer, String> producer;
    private static int PRODUCER_MESSAGE_COUNT = 1;


    @Inject
    public IndexResource(KafkaProducer<Integer, String> producer) {
        this.producer = requireNonNull(producer);
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

}

/**
 * Custom request index message
 */
class RequestIndexMessage {
    private String message;
    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }
}