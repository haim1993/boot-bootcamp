package jersey.rest;

import generator.Generator;
import mybatis.account.AccountMapper;
import pojo.Account;

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
    public Response createAccount(@QueryParam("accountName") String accountName) {
        // TODO: check if name is valid with Regex
        if (accountMapper.getAccountByName(accountName) != null) {
            return Response.status(HttpURLConnection.HTTP_CONFLICT).entity("The account name '" + accountName + "' already exists.").build();
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

        accountMapper.insertAccount(new Account(accountName, token, esIndexName));
        return Response.status(HttpURLConnection.HTTP_OK).entity("Success").build();
    }

}
