import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Random;

public class ResourceTest {
    public static RestHandler handler;

    @BeforeClass
    public static void initializeGlobalParams() {
        handler = new RestHandler();
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

    @Test
    public void testRandomGeneratedIndex() {
        String randomString = generateString(new Random(), SOURCES, 100);

        Response indexResponse = handler.indexRequestWithCustomMessage(randomString);
        assertNotNull(indexResponse);
        assertTrue(indexResponse.getStatus() == HttpURLConnection.HTTP_OK);

        // Need to sleep a bit, in order to read the current indexed message
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Response searchResponse = handler.searchRequestWithCustomMessage(randomString);
        String entity = searchResponse.readEntity(String.class);
        assertNotNull(searchResponse);
        assertTrue(searchResponse.getStatus() == HttpURLConnection.HTTP_OK);

        // Does this message exist?
        assertTrue(entity.indexOf(randomString) > -1);
    }
}
