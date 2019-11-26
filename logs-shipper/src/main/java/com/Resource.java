package com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Singleton
@Path("bootcamp")
public class Resource {

    private LogsConfiguration logsConfiguration;
    private String seed;

    public static int VISITS_COUNTER = 1;

    @Inject
    public Resource(LogsConfiguration logsConfiguration) {
        this.logsConfiguration = logsConfiguration;

        // Generate unique random seed
        this.seed = UUID.randomUUID().toString();
    }

    @GET
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

}