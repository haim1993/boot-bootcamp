import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import juice.modules.IndexerModule;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;


@Singleton
public class IndexConsumer {

    private static final String TOPIC = "sample";
    private static final String INDEX = "posts";

    private final RestHighLevelClient elasticSearchClient;
    private final KafkaConsumer<Integer, String> consumer;

    @Inject
    public IndexConsumer(RestHighLevelClient elasticSearchClient, KafkaConsumer<Integer, String> consumer) {
        this.elasticSearchClient = elasticSearchClient;
        this.consumer = consumer;
    }


    public void run() {
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));

        while (true) {
            final ConsumerRecords<Integer, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));

            // TODO: set a graceful shutdown / break for loop

            // TODO: use BulkRequest instead of single IndexRequest
//            BulkRequest request = new BulkRequest();

            ObjectMapper mapper = new ObjectMapper();
            consumerRecords.forEach(record -> {
                try {
//                    request.add(new IndexRequest(INDEX).source(jsonMap));

                    Map<String, String> jsonMap = mapper.readValue(record.value(), Map.class);
                    IndexRequest indexRequest = new IndexRequest(INDEX, "_doc");
                    indexRequest.source(jsonMap);

                    IndexResponse res =  elasticSearchClient.index(indexRequest, RequestOptions.DEFAULT);
                    System.out.printf("Consumer Record:(%d, %s, %d, %d)\n", record.key(), record.value(),
                            record.partition(), record.offset());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

//            try {
//                BulkResponse bulkResponse = elasticSearchClient.bulk(request, RequestOptions.DEFAULT);
//                if (bulkResponse.hasFailures()) {
//                    System.out.println("Bulk: There's a problem master...");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            consumer.commitAsync();
        }
//        consumer.close();
    }


    public static void main(String[] args) {
        try {
            Guice.createInjector(new IndexerModule())
                    .getInstance(IndexConsumer.class).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
