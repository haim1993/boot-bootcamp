package jersey.rest;

import api.AccountsServiceApi;
import config.GlobalParams;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import pojo.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class SearchResource {

    private final RestHighLevelClient elasticSearchClient;
    private final AccountsServiceApi accountsServiceApi;

    @Inject
    public SearchResource(RestHighLevelClient elasticSearchClient) {
        this.elasticSearchClient = requireNonNull(elasticSearchClient);
        this.accountsServiceApi = new AccountsServiceApi();
    }

    @GET
    @Path("/api/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@HeaderParam (GlobalParams.X_ACCOUNT_TOKEN) String tokenHeader,
                           @QueryParam("message") String message,
                           @QueryParam("header") String header) {
        Optional<Account> optionalAccount = accountsServiceApi.getAccountByToken(tokenHeader);
        if (!optionalAccount.isPresent()) {
            return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED)
                    .entity("The account token is not authorized").build();
        }

        SearchRequest searchRequest = new SearchRequest(optionalAccount.get().getAccountEsIndexName());

        Tuple<String, String> messagePair    = new Tuple<>("message", message);
        Tuple<String, String> agentPair      = new Tuple<>(GlobalParams.USER_AGENT, header);
        searchRequest.source(getMatchQuerySourceBuilder(messagePair, agentPair));

        Optional<String> jsonStringOptional = getHitsResultsAsString(searchRequest);
        if (!jsonStringOptional.isPresent()) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Bad request").build();
        }
        return Response.ok().entity(jsonStringOptional.get()).build();
    }

    /**
     * Given unknown args of Tuple's, containing key pairs of arguments,
     * we filter through the results that suffice all matched conditions.
     *
     * We construct a SearchSourceBuilder and return an instance of it, with the
     * filters that we calcuated.
     *
     * @param args
     * @return
     */
    private SearchSourceBuilder getMatchQuerySourceBuilder(Tuple<String,String>... args) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for(Tuple<String,String> p : args) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(p.v1(), p.v2()));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }

    /**
     * Given a SearchRequest object, we iterate through the results and concatenate them
     * all to a single string, we ultimately return.
     *
     * @param searchRequest
     * @return
     */
    private Optional<String> getHitsResultsAsString(SearchRequest searchRequest) {
        SearchResponse searchResponse = null;
        try {
            searchResponse = elasticSearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();

            StringBuilder builder = new StringBuilder();
            for (SearchHit hit : hits) {
                builder.append(hit.getSourceAsString() + "\n");
            }
            return Optional.ofNullable(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
