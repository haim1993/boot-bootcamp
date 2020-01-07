package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class KafkaConsumerModule extends AbstractModule {

    private final String kafkaConsumerFilePath;

    public KafkaConsumerModule(String kafkaConsumerFilePath) {
        this.kafkaConsumerFilePath = kafkaConsumerFilePath;
    }

    @Provides
    public KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = ConfigurationFactory.load(kafkaConsumerFilePath, Properties.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}