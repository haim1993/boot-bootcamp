package client;

import generator.Generator;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Account;

import java.util.Optional;

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
        Optional<Account> optionalAccount = accountsServiceApi.createAccount(Generator.generateName());
        assertTrue(optionalAccount.isPresent());
        String token = optionalAccount.get().getAccountToken();
        Optional<Account> ac = accountsServiceApi.getAccountByToken(token);
        assertNotNull(ac);
        assertTrue(token.equals(ac.get().getAccountToken()));
    }

}
