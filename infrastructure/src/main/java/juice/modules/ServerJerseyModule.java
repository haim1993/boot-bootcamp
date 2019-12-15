package juice.modules;

import com.google.inject.AbstractModule;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

public class ServerJerseyModule extends AbstractModule {

    private final String fileUrl;

    public ServerJerseyModule(String fileUrl) {
        this.fileUrl = fileUrl;
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
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        ServerConfiguration serverConfig = configurationFactory.load(fileUrl, ServerConfiguration.class);

        return JerseyConfiguration.builder()
                .addPackage("jersey.rest")
                .addPort(serverConfig.getPort())
                .build();
    }

}