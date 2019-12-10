package juice.modules;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import config.ConfigurationFactory;
import config.ElasticSearchConfiguration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearchModule extends AbstractModule {

    private static final String ES_CONFIGURATION_FILE_NAME = "elastic.config";
    private static final String SCHEME_HTTP_CONFIG = "http";

    @Override
    protected void configure() { }

    @Provides
    public RestHighLevelClient getRestHighLevelClient() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();

        ElasticSearchConfiguration elastic = configurationFactory.load(
//                getClass().getClassLoader().getResource(ES_CONFIGURATION_FILE_NAME).getFile(),
                ES_CONFIGURATION_FILE_NAME,
                ElasticSearchConfiguration.class);

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elastic.getHost(), elastic.getPort(), SCHEME_HTTP_CONFIG)));
    }

}

