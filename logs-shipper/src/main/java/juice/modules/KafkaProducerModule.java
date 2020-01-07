package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerModule extends AbstractModule {

    private final String kafkaProducerFilePath;

    public KafkaProducerModule(String kafkaProducerFilePath) {
        this.kafkaProducerFilePath = kafkaProducerFilePath;
    }

    @Provides
    public KafkaProducer<String, String> getKafkaProducer() {
        Properties properties = ConfigurationFactory.load(kafkaProducerFilePath, Properties.class);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(properties);
    }
}
