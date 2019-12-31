package jersey.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        if (!RegexValidator.isTokenValid(accountToken)) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity("The account token contains unsupported characters").build();
        }

        Account account = accountMapper.getAccountByToken(accountToken);
        if (account == null) {
            return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED)
                    .entity("The account token is not authorized").build();
        }

        try {
            return Response.status(HttpURLConnection.HTTP_OK).entity(account.toJsonString()).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity("Task Failed").build();
    }

}
