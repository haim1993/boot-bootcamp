package client;

import config.ConfigurationFactory;
import pojo.Account;
import regex.RegexValidator;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Optional;

public class AccountsServiceApi {

    private AccountsServiceClient accountsServiceClient;

    public AccountsServiceApi() {
        accountsServiceClient = new AccountsServiceClient();
    }

    public AccountsServiceApi(String hostName, int port) {
        accountsServiceClient = new AccountsServiceClient(hostName, port);
    }


    /**
     * Given an account token, we get the corresponding account by
     * calling the 'accounts-service' micro service.
     *
     * @param accountToken - token of account
     * @return
     */
    public Optional<Account> getAccountByToken(String accountToken) {
        if (RegexValidator.isTokenValid(accountToken)) {
            Response res = accountsServiceClient.getAccountByToken(accountToken);
            switch(res.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    return Optional.ofNullable(ConfigurationFactory.read(res.readEntity(String.class), Account.class));
            }
        }
        return Optional.empty();
    }

    public Optional<Account> createAccount(String accountName) {
        if (RegexValidator.isNameValid(accountName)) {
            Response res = accountsServiceClient.createAccount(accountName);
            switch(res.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    return Optional.ofNullable(ConfigurationFactory.read(res.readEntity(String.class), Account.class));
            }
        }
        return Optional.empty();
    }

}
