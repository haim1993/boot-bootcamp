package juice.modules;

import com.google.inject.AbstractModule;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

public class ServerJerseyModule extends AbstractModule {
//    DEBUGGING ON LOCALHOST
//    private static final String SERVER_CONFIGURATION_FILE_NAME = "./infrastructure/build/resources/main/server.config";
    private static final String SERVER_CONFIGURATION_FILE_NAME = "server.config";

    @Override
    protected void configure() {
        install(new JerseyModule(createJerseyConfiguration()));
    }

    /**
     * Creates the configuration for the JerseyModule instance that is being
     * installed and created in our custom model juice.modules.ServerModule
     * @return
     */
    private JerseyConfiguration createJerseyConfiguration() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        ServerConfiguration serverConfig = configurationFactory.load(
                SERVER_CONFIGURATION_FILE_NAME,
                ServerConfiguration.class);

        return JerseyConfiguration.builder()
                .addPackage("jersey.rest")
                .addPort(serverConfig.getPort())
                .build();
    }

}