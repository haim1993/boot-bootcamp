package jersey.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class IndexResource {

    private static final String TOPIC = "sample";

    private final KafkaProducer<Integer, String> producer;
    private final ObjectMapper mapper;

    @Inject
    public IndexResource(KafkaProducer<Integer, String> producer) {
        this.producer = requireNonNull(producer);
        this.mapper = new ObjectMapper();
    }

    @POST
    @Path("/api/index")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(RequestIndexMessage requestIndexMessage, @HeaderParam("user-agent") String userAgent) {
        Map<String, Object> documentAsJsonMap = buildDocumentAsJsonMap(requestIndexMessage, userAgent);
        if (documentAsJsonMap == null) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("The given object is null\n").build();
        }

        try {
            String msg = mapper.writeValueAsString(documentAsJsonMap);

            producer.send(new ProducerRecord<>(TOPIC, msg));

            Logger logger = LogManager.getLogger(IndexResource.class);
            logger.debug("Sent message: " + msg);

            return Response.status(HttpURLConnection.HTTP_OK).entity("Your message has been sent successfully\n").build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Your message could not be delivered\n").build();
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
    private Map<String, Object> buildDocumentAsJsonMap(RequestIndexMessage requestIndexMessage, String userAgent) {
        String msg = requestIndexMessage.getMessage();
        if (msg == null) return null;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("message", msg);
        jsonAsMap.put("User-Agent", userAgent);
        return jsonAsMap;
    }

    /**
     * Custom request index message
     */
    static class RequestIndexMessage {
        private String message;
        public String getMessage() { return this.message; }
        public void setMessage(String message) { this.message = message; }
    }
}