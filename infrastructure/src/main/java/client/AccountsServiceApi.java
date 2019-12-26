package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.Account;
import pojo.AccountConfigurations;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class AccountsServiceApi {

    private AccountsServiceClient accountsServiceClient;

    public AccountsServiceApi() {
        accountsServiceClient = new AccountsServiceClient();
    }

    public AccountsServiceApi(String hostName, int port) {
        accountsServiceClient = new AccountsServiceClient(hostName, port);
    }


    /**
     *
     * @param accountToken
     * @return
     */
    public Account getAccountByToken(String accountToken) {
        Response res = accountsServiceClient.getAccountByToken(accountToken);
        switch(res.getStatus()) {
            case HttpURLConnection.HTTP_OK:
                return parseFromJsonToAccount(res.readEntity(String.class));
        }
        return null;
    }

    /**
     *
     * @param json
     * @return
     */
    private Account parseFromJsonToAccount(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // convert JSON string to Map
            Map<String, Object> map = mapper.readValue(json, Map.class);
            Account acc = new Account();
            acc.setAccountNo((int) map.get(AccountConfigurations.ID));
            acc.setAccountName((String) map.get(AccountConfigurations.NAME));
            acc.setAccountToken((String) map.get(AccountConfigurations.TOKEN));
            acc.setAccountEsIndexName((String) map.get(AccountConfigurations.ES_INDEX_NAME));
            return acc;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
