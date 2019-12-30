import client.AccountsServiceApi;
import client.LogsShipperClient;
import generator.Generator;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Account;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexAndQueryE2ETest {
    public static LogsShipperClient handler;

    @BeforeClass
    public static void initializeGlobalParams() {
        handler = new LogsShipperClient();
    }

    @Test
    public void indexAndSearchForMessageWithTokenTest() {
        String token = createNewAccountAndGetToken();
        String randomMessage = Generator.generateMessage(100);
        indexCustomMessageWithToken(token, randomMessage);

        await().atMost(Duration.ofSeconds(3)).until(()->{
            String res = searchAndVerifyCustomMessageWithToken(token, randomMessage);
            return containsOnce(res, randomMessage);
        });
    }

    @Test
    public void indexAndSearchForMessageWithTwoTokensWithoutConflictTest() {
        String token1 = createNewAccountAndGetToken();
        String token2 = createNewAccountAndGetToken();

        String randomMessage1 = Generator.generateMessage(100);
        String randomMessage2 = Generator.generateMessage(100);

        indexCustomMessageWithToken(token1, randomMessage1);
        indexCustomMessageWithToken(token2, randomMessage2);

        await().atMost(Duration.ofSeconds(3)).until(()->
                containsOnce(searchAndVerifyCustomMessageWithToken(token1, randomMessage1), randomMessage1) &&
                !searchAndVerifyCustomMessageWithToken(token1, randomMessage2).contains(randomMessage2));

        await().atMost(Duration.ofSeconds(3)).until(()->
                containsOnce(searchAndVerifyCustomMessageWithToken(token2, randomMessage2), randomMessage2) &&
                !searchAndVerifyCustomMessageWithToken(token2, randomMessage1).contains(randomMessage1));
    }


    private String createNewAccountAndGetToken() {
        Optional<Account> optionalAccount = new AccountsServiceApi("localhost", 8888).createAccount(Generator.generateName());
        assertTrue(optionalAccount.isPresent());
        return optionalAccount.get().getAccountToken();
    }

    private void indexCustomMessageWithToken(String token, String customMessage) {
        Response indexResponse1 = handler.indexRequestWithCustomMessage(token, customMessage);
        assertNotNull(indexResponse1);
        assertTrue(indexResponse1.getStatus() == HttpURLConnection.HTTP_OK);
    }

    private String searchAndVerifyCustomMessageWithToken(String token, String customMessage) {
        Response searchResponse = handler.searchRequestWithCustomMessage(token, customMessage);
        assertNotNull(searchResponse);
        assertTrue(searchResponse.getStatus() == HttpURLConnection.HTTP_OK);
        return searchResponse.readEntity(String.class);
    }

    // Returns true if string 'substring' is in string 's' only once. Otherwise returns false
    private boolean containsOnce(final String s, final String substring) {
        Pattern pattern = Pattern.compile(substring);
        Matcher matcher = pattern.matcher(s);
        if(matcher.find()){
            return !matcher.find();
        }
        return false;
    }
}
