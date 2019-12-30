package jersey.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import generator.Generator;
import mybatis.account.AccountMapper;
import pojo.Account;
import regex.RegexValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;


@Singleton
@Path("/")
public class CreateAccountResource {

    private final AccountMapper accountMapper;

    @Inject
    public CreateAccountResource(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @POST
    @Path("/create-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(RequestAccountName requestAccountName) {
        String accountName = requestAccountName.getAccountName();

        if (!RegexValidator.isNameValid(accountName)) {
            Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity("The account name '" + accountName + "' contains unsupported characters").build();
        }

        if (accountMapper.getAccountByName(accountName) != null) {
            return Response.status(HttpURLConnection.HTTP_CONFLICT).entity("The account name '" + accountName + "' already exists").build();
        }

        // Generate UNIQUE Token
        String token = "";
        do {
            token = Generator.generateToken();
        }
        while (accountMapper.getAccountByToken(token) != null);

        // Generate UNIQUE Elasticsearch Index Name
        String esIndexName = "";
        do {
            esIndexName = Generator.generateElasticsearchIndexName();
        }
        while (accountMapper.getAccountByEsIndexName(esIndexName) != null);

        Account acc = new Account(accountName, token, esIndexName);
        accountMapper.insertAccount(acc);
        try {
            return Response.status(HttpURLConnection.HTTP_OK).entity(acc.toJsonString()).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity("Task Failed").build();
    }

    /**
     * Custom request account name
     */
    static class RequestAccountName {
        private String accountName;
        public String getAccountName() { return this.accountName; }
        public void setAccountName(String accountName) { this.accountName = accountName; }
    }

}
