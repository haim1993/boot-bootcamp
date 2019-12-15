package juice.modules;

import com.google.inject.AbstractModule;

import jersey.rest.IndexConsumer;

public class IndexerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ElasticSearchModule());
        install(new ServerJerseyModule());
        install(new KafkaConsumerModule());

        bind(IndexConsumer.class).asEagerSingleton();
    }

}
