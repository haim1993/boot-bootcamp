package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import config.LogsConfiguration;
import config.ProducerConfiguration;
import config.ServerConfiguration;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

import juice.modules.ElasticSearchModule;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ServerModule extends AbstractModule {

    private static final String LOGS_CONFIGURATION_FILE_NAME = "logs.config";
    private static final String PRODUCER_CONFIGURATION_FILE_NAME = "producer.config";
    private static final String SERVER_CONFIGURATION_FILE_NAME = "server.config";

    private final ConfigurationFactory configurationFactory;

    public ServerModule() {
        this.configurationFactory = new ConfigurationFactory();
    }

    @Override
    protected void configure() {
        install(new ElasticSearchModule());
        install(new JerseyModule(createJerseyConfiguration()));
    }

    @Provides
    public LogsConfiguration getLogsConfiguration() {
        return configurationFactory.load(
                // TODO: have this path work in docker and in local :
                //  local cmd: getClass().getClassLoader().getResource(LOGS_CONFIGURATION_FILE_NAME).getFile(),
                LOGS_CONFIGURATION_FILE_NAME,
                LogsConfiguration.class);
    }

    @Provides
    public KafkaProducer<Integer, String> getKafkaProducer() {
         ProducerConfiguration kafkaConfig = configurationFactory.load(
                 // TODO: have this path work in docker and in local :
                 //  local cmd: getClass().getClassLoader().getResource(PRODUCER_CONFIGURATION_FILE_NAME).getFile(),
                 PRODUCER_CONFIGURATION_FILE_NAME,
                 ProducerConfiguration.class);

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getHost() + ":" + kafkaConfig.getPort());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaConfig.getId());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(properties);
    }

    /**
     * Creates the configuration for the JerseyModule instance that is being
     * installed and created in our custom model juice.modules.ServerModule
     * @return
     */
    private JerseyConfiguration createJerseyConfiguration() {
        ServerConfiguration serverConfig = configurationFactory.load(
                // TODO: have this path work in docker and in local :
                //  local cmd: getClass().getClassLoader().getResource(SERVER_CONFIGURATION_FILE_NAME).getFile(),
                SERVER_CONFIGURATION_FILE_NAME,
                ServerConfiguration.class);

        return JerseyConfiguration.builder()
                .addPackage("jersey.rest")
                .addPort(serverConfig.getPort())
                .build();
    }


}
