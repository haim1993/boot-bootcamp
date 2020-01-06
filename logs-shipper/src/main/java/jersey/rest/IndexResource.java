package jersey.rest;

import api.AccountsServiceApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.GlobalParams;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.JsonParser;

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

    private final KafkaProducer<String, String> producer;
    private final AccountsServiceApi accountsServiceApi;
    private final ObjectMapper mapper;


    @Inject
    public IndexResource(KafkaProducer<String, String> producer) {
        this.producer = requireNonNull(producer);
        this.accountsServiceApi = new AccountsServiceApi();
        this.mapper = new ObjectMapper();
    }

    @POST
    @Path("/api/index/{accountToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(RequestIndexMessage requestIndexMessage,
                          @HeaderParam(GlobalParams.USER_AGENT) String userAgent,
                          @PathParam(GlobalParams.ACCOUNT_TOKEN) String accountToken) {

        if (!accountsServiceApi.getAccountByToken(accountToken).isPresent()) {
            return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED)
                    .entity("The account token is not authorized").build();
        }

        Map<String, Object> documentAsJsonMap = buildDocumentAsJsonMap(requestIndexMessage, userAgent);
        if (documentAsJsonMap == null) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("The given object is null").build();
        }

        String msg = JsonParser.toJsonString(documentAsJsonMap);

        producer.send(new ProducerRecord<>(TOPIC, accountToken, msg));

        Logger logger = LogManager.getLogger(IndexResource.class);
        logger.debug("Sent message: " + msg);

        return Response.ok().entity("Your message has been sent successfully").build();
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
        jsonAsMap.put(GlobalParams.USER_AGENT, userAgent);
        return jsonAsMap;
    }

    /**
     * Custom request index message
     */
    static class RequestIndexMessage {
        private String message;
        public String getMessage() { return this.message; }
    }
}