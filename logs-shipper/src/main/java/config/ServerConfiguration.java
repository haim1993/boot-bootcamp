package config;

import java.util.Map;

public class ServerConfiguration {
    private static final String PORT_CONFIG = "port";

    private int port;

    public ServerConfiguration(Map<String, Object> jsonMap) {
        this.port = (Integer) jsonMap.get(PORT_CONFIG);
    }

    public int getPort() { return this.port; }
}
