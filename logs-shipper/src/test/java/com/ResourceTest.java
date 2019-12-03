package com;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResourceTest {

    public static String REST_API_URI = "http://localhost:8080/api/";

    public static WebTarget getWebTarget() {
        return ClientBuilder.newClient().target(REST_API_URI);
    }

    @Test
    public void testIndexRequest() {
        String jsonObjectAsString = "{\"message\":\"maor\"}";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";

        Response response = getWebTarget().path("index/")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .post(Entity.json(jsonObjectAsString));

        assertNotNull(response);
        assertTrue(response.getStatus() == 201);
    }


    @Test
    public void testSearchRequest() {
        String message  = "maor";
        String header   = "Macintosh";

        Response response = getWebTarget().path("search")
                .queryParam("message", message)
                .queryParam("header", header)
                .request(MediaType.TEXT_PLAIN)
                .get();

        System.out.print(response.readEntity(String.class));

        assertTrue(response.getStatus() == 200);
    }
}
