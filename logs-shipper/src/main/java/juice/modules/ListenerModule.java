package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import config.LogsConfiguration;

public class ListenerModule extends AbstractModule {

    private static final String LOGS_CONFIGURATION_FILE_NAME = "logs.config";

    @Override
    protected void configure() {
        install(new ElasticSearchModule());
        install(new ServerJerseyModule());
        install(new KafkaProducerModule());
    }

    @Provides
    public LogsConfiguration getLogsConfiguration() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        return configurationFactory.load(
                LOGS_CONFIGURATION_FILE_NAME,
                LogsConfiguration.class);
    }



}
