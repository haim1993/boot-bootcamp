package client;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AccountsServiceClient {


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
     * HttpURLConnection.HTTP_BAD_REQUEST ( 400 ) - The token 'accountToken' does not exist
     * HttpURLConnection.HTTP_NOT_FOUND ( 404 ) - The json mapper has thrown an exception
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
     * HttpURLConnection.HTTP_CONFLICT ( 409 ) - Account name already exists.
     */
    public Response createAccount(String accountName) {
        return webTarget.path("create-account")
                .queryParam("accountName", accountName)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(""));
    }

}
