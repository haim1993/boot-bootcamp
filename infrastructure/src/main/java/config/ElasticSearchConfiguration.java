package config;

import java.util.Map;

public class ElasticSearchConfiguration {
    private static final String HOST_CONFIG = "host";
    private static final String PORT_CONFIG = "port";

    private String host;
    private int port;

    public ElasticSearchConfiguration(Map<String, Object> jsonMap) {
        this.host = (String) jsonMap.get(HOST_CONFIG);
        this.port = (Integer) jsonMap.get(PORT_CONFIG);
    }

    public String getHost() { return this.host; }
    public int getPort() { return this.port; }
}
