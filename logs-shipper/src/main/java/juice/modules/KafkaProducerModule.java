package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import config.ProducerConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerModule extends AbstractModule {
//    DEBUGGING ON LOCALHOST
//    private static final String PRODUCER_CONFIGURATION_FILE_NAME = "./logs-shipper/build/resources/main/producer.config";
    private static final String PRODUCER_CONFIGURATION_FILE_NAME = "producer.config";

    @Provides
    public KafkaProducer<Integer, String> getKafkaProducer() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
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
