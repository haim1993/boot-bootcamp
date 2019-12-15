package jersey.rest;

import config.LogsConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class LogsShipperResource {

    // Generate unique random seed
    private static final String SEED = UUID.randomUUID().toString();
    private static int VISITS_COUNTER = 1;

    private LogsConfiguration logsConfiguration;

    @Inject
    public LogsShipperResource(LogsConfiguration logsConfiguration) {
        this.logsConfiguration = requireNonNull(logsConfiguration);
    }

    @GET
    @Path("/logs")
    @Produces(MediaType.TEXT_PLAIN)
    public String logShipper() {
        String response =   "{ " +
                    "Seed: " + SEED + ", " +
                    "Visits: " + (VISITS_COUNTER++ + ", " +
                    "LogMessage: " + logsConfiguration.getLogMessage() +
                " }");

        Logger logger = LogManager.getLogger(LogsShipperResource.class);
        logger.info(response);

        return response;
    }
}
