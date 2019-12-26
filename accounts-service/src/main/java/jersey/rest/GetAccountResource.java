package jersey.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mybatis.account.AccountMapper;
import pojo.Account;
import pojo.AccountConfigurations;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Path("/account")
public class GetAccountResource {

    private final AccountMapper accountMapper;

    @Inject
    public GetAccountResource(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @GET
    @Path("/token/{accountToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountByToken(@PathParam("accountToken") String accountToken) {

        // TODO: check regex token
        Account a = (Account) accountMapper.getAccountByToken(accountToken);
        if (a == null) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("The token does not exist.").build();
        }

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(AccountConfigurations.ID, a.getAccountNo());
        jsonMap.put(AccountConfigurations.NAME, a.getAccountName());
        jsonMap.put(AccountConfigurations.TOKEN, a.getAccountToken());
        jsonMap.put(AccountConfigurations.ES_INDEX_NAME, a.getAccountEsIndexName());

        final ObjectMapper mapper = new ObjectMapper();

        try {
            return Response.status(HttpURLConnection.HTTP_OK).entity(mapper.writeValueAsString(jsonMap)).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity("Task Failed.").build();
    }

}
