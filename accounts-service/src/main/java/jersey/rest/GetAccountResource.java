package jersey.rest;

import config.GlobalParams;
import mybatis.account.AccountMapper;
import pojo.Account;
import regex.RegexValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/account")
public class GetAccountResource {

    private final AccountMapper accountMapper;

    @Inject
    public GetAccountResource(AccountMapper accountMapper) {
        this.accountMapper = requireNonNull(accountMapper);
    }

    @GET
    @Path("/token/{accountToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountByToken(@PathParam(GlobalParams.ACCOUNT_TOKEN) String accountToken) {
        if (!RegexValidator.isTokenValid(accountToken)) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity("The account token contains unsupported characters").build();
        }

        Account account = accountMapper.getAccountByToken(accountToken);
        if (account == null) {
            return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED)
                    .entity("The account token is not authorized").build();
        }
        return Response.ok().entity(account).build();
    }

}
