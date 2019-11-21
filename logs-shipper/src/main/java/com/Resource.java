package com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("boot-bootcamp")
public class Resource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sendLog() {
        String response = "Seed: " + SimpleServer.SEED + ", Number of visits: " + (SimpleServer.VISITS_COUNTER++);

        Logger logger = LogManager.getLogger(Resource.class);
        logger.info(response);

        return response;
    }

}