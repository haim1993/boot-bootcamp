package config;

import java.util.Map;

public class LogsConfiguration {

    private static final String LOG_MESSAGE_CONFIG = "logMessage";

    private String logMessage;

    public LogsConfiguration(Map<String, Object> jsonMap) {
        this.logMessage = (String) jsonMap.get(LOG_MESSAGE_CONFIG);
    }

    public String getLogMessage() { return this.logMessage; }
}
