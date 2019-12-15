package jersey.rest;

import consumer.IndexConsumer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class GracefulShutdownResource {

    private final IndexConsumer consumer;

    @Inject
    public GracefulShutdownResource(IndexConsumer consumer) {
        this.consumer = requireNonNull(consumer);
    }

    @POST
    @Path("close")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response close() {
        consumer.close();
        return Response.status(HttpURLConnection.HTTP_OK).entity("Consumer is being closed gracefully :)\n").build();
    }

}
