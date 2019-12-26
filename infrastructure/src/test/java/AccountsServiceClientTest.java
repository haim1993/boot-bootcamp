import client.AccountsServiceClient;
import org.junit.BeforeClass;
import org.junit.Test;

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
        Response res = handler.getAccountByToken("tAEYfUvoVSiumBkPrEHVsonpNTbHQXkh");
        System.out.println(res.readEntity(String.class));
    }


    @Test
    public void createAccountWithRandomGeneratedNameTest() {
        // TODO: create random name generator
        Response res = handler.createAccount("ava");
        System.out.println(res.readEntity(String.class));
        assertTrue(res.getStatus() == HttpURLConnection.HTTP_OK);
    }

    @Test
    public void createAccountWithExistingNameTest() {
        // TODO: here i need to add a name, then try to add it again. best to test it when
        //  adding new random name twice
        Response res1 = createAccount("ava");
        Response res2 = createAccount("ava");
        System.out.println(res1.readEntity(String.class));
        assertTrue(res1.getStatus() == HttpURLConnection.HTTP_CONFLICT);
    }

    private Response createAccount(String accountName) {
        return handler.createAccount(accountName);
    }
}
