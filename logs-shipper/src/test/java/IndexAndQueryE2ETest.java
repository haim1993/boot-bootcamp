import client.LogsShipperClient;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.time.Duration;
import java.util.Random;

public class IndexAndQueryE2ETest {
    public static LogsShipperClient handler;

    @BeforeClass
    public static void initializeGlobalParams() {
        handler = new LogsShipperClient();
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
    private String generateString(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

    @Test
    public void testRandomGeneratedIndex() {
        // TODO: First run, i get 504, second run works. Check this!!!!!

        String randomString = generateString(new Random(), SOURCES, 100);
        String token = "root";

        Response indexResponse = handler.indexRequestWithCustomMessage(token, randomString);
        assertNotNull(indexResponse);
        System.out.println(indexResponse.getStatus());
        assertTrue(indexResponse.getStatus() == HttpURLConnection.HTTP_OK);

        await().atMost(Duration.ofSeconds(3)).until(()->{
            Response searchResponse = handler.searchRequestWithCustomMessage(token, randomString);
            assertNotNull(searchResponse);
            String entity = searchResponse.readEntity(String.class);
            boolean isMessageIndexed = searchResponse.getStatus() == HttpURLConnection.HTTP_OK;
            boolean isMessageFound = entity.indexOf(randomString) > -1;

            return isMessageIndexed && isMessageFound;
        });
    }
}
