package client;

import config.ConfigurationFactory;
import generator.Generator;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Account;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

import static junit.framework.TestCase.assertTrue;

public class AccountsServiceClientTest {


    public static AccountsServiceClient handler;

    @BeforeClass
    public static void initializeGlobalParams() {
        handler = new AccountsServiceClient("localhost", 8888);
    }

    @Test
    public void getAccountByTokenTest() {
        Account randomAccount = createRandomAccount();
        Response res = handler.getAccountByToken(randomAccount.getAccountToken());
        String jsonAccount = res.readEntity(String.class);
        Account account = ConfigurationFactory.read(jsonAccount, Account.class);
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_OK);
        assertTrue(account.getAccountToken().equals(randomAccount.getAccountToken()));
    }

    @Test
    public void getAccountByUnsupportedTokenTest() {
        String token = "12521f21f12512d12egewgewhew";
        Response res = handler.getAccountByToken(token);
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    public void getAccountByUnauthorizedTokenTest() {
        String token = "aaaaabbbbbcccccdddddeeeeefffffAB";
        Response res = handler.getAccountByToken(token);
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @Test
    public void createUnsupportedAccountNameTest() {
        Response res = handler.createAccount("DROP TABLE Account");
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    public void createAccountNameWithConflictTest() {
        Response res = handler.createAccount("root");
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_CONFLICT);
    }

    private Account createRandomAccount() {
        Response res = handler.createAccount(Generator.generateName());
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_OK);
        return ConfigurationFactory.read(res.readEntity(String.class), Account.class);
    }
}
