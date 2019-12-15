package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import config.ConsumerConfiguration;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class KafkaConsumerModule extends AbstractModule {

    private final String fileUrl;

    public KafkaConsumerModule(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Provides
    public KafkaConsumer<Integer, String> getKafkaConsumer() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        ConsumerConfiguration consumerConfig = configurationFactory.load(fileUrl, ConsumerConfiguration.class);

        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.getHost() + ":" + consumerConfig.getPort());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.getId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}
