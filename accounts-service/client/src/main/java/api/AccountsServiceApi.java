package api;

import parser.JsonParser;
import pojo.Account;
import pojo.CreateAccountRequest;
import regex.RegexValidator;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
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
                    return Optional.ofNullable(JsonParser.fromJsonString(res.readEntity(String.class), Account.class));
            }
        }
        return Optional.empty();
    }

    public Optional<Account> createAccount(String accountName) {
        if (RegexValidator.isNameValid(accountName)) {
            Response res = accountsServiceClient.createAccount(accountName);
            switch(res.getStatus()) {
                case HttpURLConnection.HTTP_OK:
                    return Optional.ofNullable(JsonParser.fromJsonString(res.readEntity(String.class), Account.class));
            }
        }
        return Optional.empty();
    }

    static class AccountsServiceClient {
        private static final String HTTP = "http";
        private static final String DEFAULT_SERVICE_HOST_NAME = "accounts";
        private static final int DEFAULT_PORT = 8080;

        private WebTarget webTarget;
        private String serviceHostName;
        private int port;

        public AccountsServiceClient() {
            this.serviceHostName = DEFAULT_SERVICE_HOST_NAME;
            this.port = DEFAULT_PORT;
            initWebTarget();
        }

        public AccountsServiceClient(String hostName, int port) {
            this.serviceHostName = hostName;
            this.port = port;
            initWebTarget();
        }

        private void initWebTarget() {
            this.webTarget = ClientBuilder.newClient().target(HTTP+"://"+this.serviceHostName+":"+this.port+"/");
        }

        /**
         * Given a token of an account, we return the account details as a response in json format
         *
         * @param accountToken - the given token, as input
         * @return
         *
         * Response status code:
         *
         * HttpURLConnection.HTTP_OK ( 200 ) - The token 'accountToken' was found
         * HttpURLConnection.HTTP_BAD_REQUEST ( 400 ) - The token 'accountToken' contains unsupported characters
         * HttpURLConnection.HTTP_UNAUTHORIZED ( 401 ) - The given token is not authorized
         */
        public Response getAccountByToken(String accountToken) {
            return webTarget.path("account/token/" + accountToken)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
        }

        /**
         * Creates a new unique account with unique fields
         *
         * @param accountName - the given name, as input
         * @return
         *
         * Response status code:
         *
         * HttpURLConnection.HTTP_OK ( 200 ) - The account was successfully created.
         * HttpURLConnection.HTTP_BAD_REQUEST ( 400 ) - The account name 'accountName' contains unsupported characters
         * HttpURLConnection.HTTP_CONFLICT ( 409 ) - Account name already exists.
         */
        public Response createAccount(String accountName) {
            String jsonObject = JsonParser.toJsonString(new CreateAccountRequest(accountName));
            return webTarget.path("create-account")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(jsonObject));
        }
    }

}
