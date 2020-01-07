package consumer;

import api.AccountsServiceApi;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import parser.JsonParser;
import pojo.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

@Singleton
public class IndexConsumer implements Closeable {

    private static final Logger logger = LogManager.getLogger(IndexConsumer.class);
    private static final String TOPIC = "sample";

    private final RestHighLevelClient elasticSearchClient;
    private final KafkaConsumer<String, String> consumer;

    private Boolean consumingState;
    private ExecutorService executorService;

    private final AccountsServiceApi accountsServiceApi;

    @Inject
    public IndexConsumer(RestHighLevelClient elasticSearchClient, KafkaConsumer<String, String> consumer) {
        this.elasticSearchClient = requireNonNull(elasticSearchClient);
        this.consumer = requireNonNull(consumer);
        this.consumingState = true;

        this.accountsServiceApi = new AccountsServiceApi();

        // Run the consumer client
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.submit(this::run);
    }

    private void run() {
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));

        while (consumingState) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            if (consumerRecords.isEmpty()) continue;

            BulkRequest request = getBulkRequestByConsumerRecords(consumerRecords);
            if (request.numberOfActions() == 0) continue;
            try {
                BulkResponse bulkResponse = elasticSearchClient.bulk(request, RequestOptions.DEFAULT);
                if (bulkResponse.hasFailures()) {
                    logger.error("Bulk: There's a problem master...");
                }
                consumer.commitSync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        consumer.close();
    }

    @Override
    public void close() {
        this.consumingState = false;
    }

    /**
     * Given the ConsumerRecords, we iterate via the results (if exists) and return
     * an object of type BulkRequest after indexing all the records.
     *
     * @param consumerRecords
     * @return
     */
    private BulkRequest getBulkRequestByConsumerRecords(ConsumerRecords<String, String> consumerRecords) {
        BulkRequest request = new BulkRequest();
        consumerRecords.forEach(record -> {
            Map<String, String> jsonMap = JsonParser.fromJsonString(record.value(), Map.class);

            Optional<Account> optionalAccount = accountsServiceApi.getAccountByToken(record.key());
            if (optionalAccount.isPresent()) {
                request.add(new IndexRequest(optionalAccount.get().getAccountEsIndexName(), "_doc").source(jsonMap));
            }
        });
        return request;
    }
}
