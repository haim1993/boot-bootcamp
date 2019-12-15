package juice.modules;

import com.google.inject.AbstractModule;

import consumer.IndexConsumer;

public class IndexerModule extends AbstractModule {

//    DEBUGGING ON LOCALHOST
//    private static final String SERVER_CONFIGURATION_FILE_NAME = "./infrastructure/build/resources/main/server.config";
//    private static final String CONSUMER_CONFIGURATION_FILE_NAME = "./indexer/build/resources/main/consumer.config";

    private static final String SERVER_CONFIGURATION_FILE_NAME = "server.config";
    private static final String CONSUMER_CONFIGURATION_FILE_NAME = "consumer.config";

    @Override
    protected void configure() {
        install(new ElasticSearchModule());
        install(new ServerJerseyModule(SERVER_CONFIGURATION_FILE_NAME));
        install(new KafkaConsumerModule(CONSUMER_CONFIGURATION_FILE_NAME));

        bind(IndexConsumer.class).asEagerSingleton();
    }

}
