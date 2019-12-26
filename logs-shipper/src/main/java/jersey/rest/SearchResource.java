package jersey.rest;

import client.AccountsServiceApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class SearchResource {

    private final RestHighLevelClient elasticSearchClient;

    @Inject
    public SearchResource(RestHighLevelClient elasticSearchClient) {
        this.elasticSearchClient = requireNonNull(elasticSearchClient);
    }

    @GET
    @Path("/api/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@HeaderParam ("X-ACCOUNT-TOKEN") String tokenHeader, @QueryParam("message") String message, @QueryParam("header") String header) {
        Tuple<String, String> messagePair    = new Tuple<>("message", message);
        Tuple<String, String> agentPair      = new Tuple<>("User-Agent", header);

        // TODO: check token regex

        Logger logger = LogManager.getLogger(SearchResource.class);
        logger.debug("Token: " + tokenHeader);

        Account acc = new AccountsServiceApi().getAccountByToken(tokenHeader);

        SearchRequest searchRequest = new SearchRequest(acc.getAccountEsIndexName());
        searchRequest.source(getMatchQuerySourceBuilder(messagePair, agentPair));

        try {
            SearchResponse searchResponse = elasticSearchClient.search( searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();

            StringBuilder builder = new StringBuilder();
            for (SearchHit hit : hits) {
                builder.append(hit.getSourceAsString() + "\n");
            }
            return Response.status(HttpURLConnection.HTTP_OK).entity(builder.toString()).build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Failed request\n").build();
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

}
