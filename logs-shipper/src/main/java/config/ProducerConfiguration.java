package config;

import java.util.Map;

public class ProducerConfiguration {
    private static final String HOST_CONFIG = "host";
    private static final String PORT_CONFIG = "port";
    private static final String ID_CONFIG = "id";

    private String host;
    private int port;
    private String id;

    public ProducerConfiguration(Map<String, Object> jsonMap) {
        this.host = (String) jsonMap.get(HOST_CONFIG);
        this.port = (Integer) jsonMap.get(PORT_CONFIG);
        this.id = (String) jsonMap.get(ID_CONFIG);
    }

    public String getHost() { return this.host; }
    public int getPort() { return this.port; }
    public String getId() { return this.id; }
}
