package com;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

public class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JerseyModule(createJerseyConfiguration()));
    }

    @Provides
    public LogsConfiguration getLogsConfiguration() {
        return Configuration.getLogsConfiguration();
    }

    /**
     * Creates the configuration for the JerseyModule instance that is being
     * installed and created in our custom model ServerModule
     * @return
     */
    private JerseyConfiguration createJerseyConfiguration() {
        return JerseyConfiguration.builder()
                .addPackage("com")
                .addPort(Configuration.getServerConfiguration().getPort())
                .build();
    }
}
