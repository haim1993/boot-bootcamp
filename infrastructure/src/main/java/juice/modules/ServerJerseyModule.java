package juice.modules;

import com.google.inject.AbstractModule;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

public class ServerJerseyModule extends AbstractModule {

    private final String serverJerseyFilePath;

    public ServerJerseyModule(String serverJerseyFilePath) {
        this.serverJerseyFilePath = serverJerseyFilePath;
    }

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
        ServerConfiguration serverConfig = ConfigurationFactory.load(
                serverJerseyFilePath,
                ServerConfiguration.class);

        return JerseyConfiguration.builder()
                .addPackage("jersey.rest")
                .addPort(serverConfig.getPort())
                .build();
    }

}