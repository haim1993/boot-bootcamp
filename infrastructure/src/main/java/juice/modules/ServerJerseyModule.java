package juice.modules;

import com.google.inject.AbstractModule;
import config.ConfigurationFactory;
import config.ServerJerseyConfiguration;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

public class ServerJerseyModule extends AbstractModule {

    private final String serverJerseyFilePath;
    private final String packageName;

    public ServerJerseyModule(String serverJerseyFilePath) {
        this.serverJerseyFilePath = serverJerseyFilePath;
        this.packageName = "jersey.rest";
    }

    public ServerJerseyModule(String serverJerseyFilePath, String packageName) {
        this.serverJerseyFilePath = serverJerseyFilePath;
        this.packageName = packageName;
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
        ServerJerseyConfiguration serverConfig = ConfigurationFactory.load(
                serverJerseyFilePath,
                ServerJerseyConfiguration.class);

        return JerseyConfiguration.builder()
                .addPackage(packageName)
                .addPort(serverConfig.getPort())
                .build();
    }

}