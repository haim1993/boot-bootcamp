package com;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ServerModule extends AbstractModule {

    final ServerConfiguration serverConfiguration = Configuration.getServerConfiguration();

    @Override
    protected void configure() {
        install(new JerseyModule(createJerseyConfiguration()));
    }

    @Provides
    public LogsConfiguration getLogsConfiguration() {
        return Configuration.getLogsConfiguration();
    }

    @Provides
    public RestHighLevelClient getRestHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(serverConfiguration.getElasticHost(), serverConfiguration.getElasticPort(), "http")));
    }

    /**
     * Creates the configuration for the JerseyModule instance that is being
     * installed and created in our custom model ServerModule
     * @return
     */
    private JerseyConfiguration createJerseyConfiguration() {
        return JerseyConfiguration.builder()
                .addPackage("com")
                .addPort(serverConfiguration.getPort())
                .build();
    }
}
