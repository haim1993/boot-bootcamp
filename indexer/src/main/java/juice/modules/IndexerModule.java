package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import config.ConfigurationFactory;
import config.ConsumerConfiguration;

import jersey.rest.IndexConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class IndexerModule extends AbstractModule {

    private static final String CONSUMER_CONFIGURATION_FILE_NAME = "consumer.config";

    @Override
    protected void configure() {
        install(new ElasticSearchModule());
        install(new ServerJerseyModule());

        bind(IndexConsumer.class).asEagerSingleton();
    }

    @Provides
    public KafkaConsumer<Integer, String> getKafkaConsumer() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        ConsumerConfiguration consumerConfig = configurationFactory.load(
                CONSUMER_CONFIGURATION_FILE_NAME,
                ConsumerConfiguration.class);

        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.getHost() + ":" + consumerConfig.getPort());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.getId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }

}
