package jersey.rest;

import generator.AccountMetaDataGenerator;
import mybatis.account.AccountMapper;
import pojo.Account;
import pojo.CreateAccountRequest;
import regex.RegexValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class CreateAccountResource {

    private final AccountMapper accountMapper;

    @Inject
    public CreateAccountResource(AccountMapper accountMapper) {
        this.accountMapper = requireNonNull(accountMapper);
    }

    @POST
    @Path("/create-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccountRequest createAccountRequest) {
        String accountName = createAccountRequest.getAccountName();

        if (!RegexValidator.isNameValid(accountName)) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity("The account name '" + accountName + "' contains unsupported characters").build();
        }

        if (accountMapper.getAccountByName(accountName) != null) {
            return Response.status(HttpURLConnection.HTTP_CONFLICT).entity("The account name '" + accountName + "' already exists").build();
        }

        // Generate UNIQUE Token
        String token = AccountMetaDataGenerator.generateToken();

        // Generate UNIQUE Elasticsearch Index Name
        String esIndexName = AccountMetaDataGenerator.generateElasticsearchIndexName();

        Account acc = new Account(accountName, token, esIndexName);

        // Mybatis sets the accountId of the object acc
        accountMapper.insertAccount(acc);

        return Response.ok().entity(acc).build();
    }
}
