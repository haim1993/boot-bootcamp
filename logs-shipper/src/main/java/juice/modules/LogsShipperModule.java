package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import config.LogsConfiguration;

public class LogsShipperModule extends AbstractModule {

//    DEBUGGING ON LOCALHOST
//    private static final String LOGS_CONFIGURATION_FILE_NAME = "./logs-shipper/build/resources/main/logs.config";
//    private static final String SERVER_CONFIGURATION_FILE_NAME = "./infrastructure/build/resources/main/server.config";
//    private static final String PRODUCER_CONFIGURATION_FILE_NAME = "./logs-shipper/build/resources/main/producer.config";

    private static final String LOGS_CONFIGURATION_FILE_NAME = "logs.config";
    private static final String SERVER_CONFIGURATION_FILE_NAME = "server.config";
    private static final String PRODUCER_CONFIGURATION_FILE_NAME = "producer.config";


    @Override
    protected void configure() {
        install(new ElasticsearchClientModule());
        install(new ServerJerseyModule(SERVER_CONFIGURATION_FILE_NAME));
        install(new KafkaProducerModule(PRODUCER_CONFIGURATION_FILE_NAME));
    }

    @Provides
    public LogsConfiguration getLogsConfiguration() {
        return ConfigurationFactory.load(
                LOGS_CONFIGURATION_FILE_NAME,
                LogsConfiguration.class);
    }

}
