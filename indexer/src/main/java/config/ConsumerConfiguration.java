package config;

import java.util.Map;

public class ConsumerConfiguration {
    private static final String HOST_CONFIG = "host";
    private static final String PORT_CONFIG = "port";
    private static final String GROUP_ID_CONFIG = "group.id";

    private String host;
    private int port;
    private String groupId;

    public ConsumerConfiguration(Map<String, Object> jsonMap) {
        this.host = (String) jsonMap.get(HOST_CONFIG);
        this.port = (Integer) jsonMap.get(PORT_CONFIG);
        this.groupId = (String) jsonMap.get(GROUP_ID_CONFIG);
    }

    public String getHost() { return this.host; }
    public int getPort() { return this.port; }
    public String getGroupId() { return this.groupId; }
}
