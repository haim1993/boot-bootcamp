package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import config.LogsConfiguration;
import config.ProducerConfiguration;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ListenerModule extends AbstractModule {

    private static final String LOGS_CONFIGURATION_FILE_NAME = "logs.config";
    private static final String PRODUCER_CONFIGURATION_FILE_NAME = "producer.config";

    private final ConfigurationFactory configurationFactory;

    public ListenerModule() {
        this.configurationFactory = new ConfigurationFactory();
    }

    @Override
    protected void configure() {
        install(new ElasticSearchModule());
        install(new ServerJerseyModule());
    }

    @Provides
    public LogsConfiguration getLogsConfiguration() {
        return configurationFactory.load(
                LOGS_CONFIGURATION_FILE_NAME,
                LogsConfiguration.class);
    }

    @Provides
    public KafkaProducer<Integer, String> getKafkaProducer() {
         ProducerConfiguration kafkaConfig = configurationFactory.load(
                 PRODUCER_CONFIGURATION_FILE_NAME,
                 ProducerConfiguration.class);

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getHost() + ":" + kafkaConfig.getPort());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaConfig.getId());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(properties);
    }

}
