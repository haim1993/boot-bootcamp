package config;

import java.util.Map;

public class ProducerConfiguration {
    private static final String HOST_CONFIG = "host";
    private static final String PORT_CONFIG = "port";
    private static final String CLIENT_ID_CONFIG = "client.id";

    private String host;
    private int port;
    private String clientId;

    public ProducerConfiguration(Map<String, Object> jsonMap) {
        this.host = (String) jsonMap.get(HOST_CONFIG);
        this.port = (Integer) jsonMap.get(PORT_CONFIG);
        this.clientId = (String) jsonMap.get(CLIENT_ID_CONFIG);
    }

    public String getHost() { return this.host; }
    public int getPort() { return this.port; }
    public String getClientId() { return this.clientId; }
}
