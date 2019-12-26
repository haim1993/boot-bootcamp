import client.AccountsServiceApi;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Account;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class AccountsServiceApiTest {

    public static AccountsServiceApi accountsServiceApi;

    @BeforeClass
    public static void initializeGlobalParams() {
        accountsServiceApi = new AccountsServiceApi("localhost", 8888);
    }

    @Test
    public void getAccountByTokenTest() {
        String token = "root";
        Account ac = accountsServiceApi.getAccountByToken(token);
        assertNotNull(ac);
        assertTrue(token.equals(ac.getAccountToken()));
    }


//    @Test
//    public void createAccountWithRandomGeneratedNameTest() {
//        // TODO: create random name generator
//        Response res = handler.createAccount("ava");
//        System.out.println(res.readEntity(String.class));
//        assertTrue(res.getStatus() == HttpURLConnection.HTTP_CONFLICT);
//    }
//
//    @Test
//    public void createAccountWithExistingNameTest() {
//        // TODO: here i need to add a name, then try to add it again. best to test it when
//        //  adding new random name twice
//        Response res1 = createAccount("ava");
//        Response res2 = createAccount("ava");
//        System.out.println(res1.readEntity(String.class));
//        assertTrue(res1.getStatus() == HttpURLConnection.HTTP_CONFLICT);
//    }
//
//    private Response createAccount(String accountName) {
//        return handler.createAccount(accountName);
//    }

}
