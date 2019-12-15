package jersey.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

@Singleton
public class IndexConsumer implements Closeable {

    private static final String TOPIC = "sample";
    private static final String INDEX = "posts";

    private final RestHighLevelClient elasticSearchClient;
    private final KafkaConsumer<Integer, String> consumer;

    private Boolean consumingState;
    private ExecutorService executorService;

    @Inject
    public IndexConsumer(RestHighLevelClient elasticSearchClient, KafkaConsumer<Integer, String> consumer) {
        this.elasticSearchClient = requireNonNull(elasticSearchClient);
        this.consumer = requireNonNull(consumer);
        this.consumingState = true;

        // Run the run
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.submit(this::run);
    }

    public void run() {
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));

        while (consumingState) {
            final ConsumerRecords<Integer, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));

            // TODO: use BulkRequest instead of single IndexRequest
            BulkRequest request = new BulkRequest();
            ObjectMapper mapper = new ObjectMapper();
            consumerRecords.forEach(record -> {
                try {
                    Map<String, String> jsonMap = mapper.readValue(record.value(), Map.class);
                    request.add(new IndexRequest(INDEX, "_doc").source(jsonMap));

                    System.out.printf("Consumer Record:(%d, %s, %d, %d)\n", record.key(), record.value(),
                            record.partition(), record.offset());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (request.numberOfActions() == 0) continue;
            try {
                BulkResponse bulkResponse = elasticSearchClient.bulk(request, RequestOptions.DEFAULT);
                if (bulkResponse.hasFailures()) {
                    System.out.println("Bulk: There's a problem master...");
                }

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
}
