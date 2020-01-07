package api;

import generator.TestGenerator;
import org.junit.BeforeClass;
import org.junit.Test;
import parser.JsonParser;
import pojo.Account;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class AccountsServiceApiTest {

    public static AccountsServiceApi.AccountsServiceClient accountsServiceClient;
    public static AccountsServiceApi accountsServiceApi;

    private final static String LOCALHOST = "localhost";
    private final static int PORT = 8888;

    @BeforeClass
    public static void initializeGlobalParams() {
        accountsServiceClient = new AccountsServiceApi.AccountsServiceClient(LOCALHOST, PORT);
        accountsServiceApi = new AccountsServiceApi(LOCALHOST, PORT);
    }

    @Test
    public void getAccountByTokenApiTest() {
        Optional<Account> optionalAccount = accountsServiceApi.createAccount(TestGenerator.generateName());
        assertTrue(optionalAccount.isPresent());
        String token = optionalAccount.get().getAccountToken();
        Optional<Account> ac = accountsServiceApi.getAccountByToken(token);
        assertNotNull(ac);
        assertTrue(token.equals(ac.get().getAccountToken()));
    }

    @Test
    public void getAccountByTokenClientTest() {
        Account randomAccount = createRandomAccount();
        Response res = accountsServiceClient.getAccountByToken(randomAccount.getAccountToken());
        String jsonAccount = res.readEntity(String.class);
        Account account = JsonParser.fromJsonString(jsonAccount, Account.class);
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_OK);
        assertTrue(account.getAccountToken().equals(randomAccount.getAccountToken()));
    }

    @Test
    public void getAccountByUnsupportedTokenTest() {
        String token = "12521f21f12512d12egewgewhew";
        Response res = accountsServiceClient.getAccountByToken(token);
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    public void getAccountByUnauthorizedTokenTest() {
        String token = "aaaaabbbbbcccccdddddeeeeefffffAB";
        Response res = accountsServiceClient.getAccountByToken(token);
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @Test
    public void createUnsupportedAccountNameTest() {
        Response res = accountsServiceClient.createAccount("DROP TABLE Account");
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    public void createAccountNameWithConflictTest() {
        Response res = accountsServiceClient.createAccount("root");
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_CONFLICT);
    }

    private Account createRandomAccount() {
        Response res = accountsServiceClient.createAccount(TestGenerator.generateName());
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_OK);
        return JsonParser.fromJsonString(res.readEntity(String.class), Account.class);
    }

}
