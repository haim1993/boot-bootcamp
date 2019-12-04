package com;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Random;

public class ResourceTest {
    public static WebTarget webTarget;

    @BeforeClass
    public static void initializeGlobalParams() {
        String REST_API_URI = "http://localhost:8080/api/";
        webTarget = ClientBuilder.newClient().target(REST_API_URI);
    }

    public static final String SOURCES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";


    /**
     * Generate a random string.
     *
     * @param random the random number generator.
     * @param characters the characters for generating string.
     * @param length the length of the generated string.
     * @return
     */
    public String generateString(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

    /**
     * Run a POST request with a custom message. We index the new message into the
     * elastic search using our custom message, using the client designed in @BeforeClass in test.
     *
     * @param message - the custom message that we index, along with a static user-agent.
     * @return
     */
    public Response indexRequestWithCustomMessage(String message) {
        String jsonObjectAsString = "{\"message\":\"" + message + "\"}";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";

        return webTarget.path("index")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .post(Entity.json(jsonObjectAsString));
    }

    /**
     * Run a GET request with a custom message. We search for patterns in
     * elastic search with the delivered message.
     *
     * @param message - the custom message that we wish to search for
     * @return
     */
    public Response searchRequestWithCustomMessage(String message) {
        String header   = "Macintosh";

        return webTarget.path("search")
                .queryParam("message", message)
                .queryParam("header", header)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    @Test
    public void testRandomGeneratedIndex() {
        String randomString = generateString(new Random(), SOURCES, 1000);

        Response indexResponse = indexRequestWithCustomMessage(randomString);
        assertNotNull(indexResponse);
        assertTrue(indexResponse.getStatus() == HttpURLConnection.HTTP_OK);

        // Need to sleep a bit, in order to read the current indexed message
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Response searchResponse = searchRequestWithCustomMessage(randomString);
        String entity = searchResponse.readEntity(String.class);
        assertNotNull(searchResponse);
        assertTrue(searchResponse.getStatus() == HttpURLConnection.HTTP_OK);

        // Does this message exist?
        assertTrue(entity.indexOf(randomString) > -1);
    }
}
