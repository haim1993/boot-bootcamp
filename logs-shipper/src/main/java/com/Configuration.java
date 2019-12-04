package com;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class Configuration {

    // If running locally, add this to prefix file "logs-shipper/src/main/resources/"
    public static String SERVER_CONFIGURATION_FILE_PATH = "server.config";
    public static String LOGS_CONFIGURATION_FILE_PATH   = "logs.config";


    /**
     * Parses the server configuration file, and returns the an instance
     * that corresponds to the values in the configuration file.
     * @return
     */
    public static ServerConfiguration getServerConfiguration() {
        JSONObject obj = parseConfigurationFile(SERVER_CONFIGURATION_FILE_PATH);
        int port = Math.toIntExact((Long) obj.get("port"));
        String elasticHost =  (String) obj.get("elasticHost");
        int elasticPort = Math.toIntExact((Long) obj.get("elasticPort"));
        return new ServerConfiguration(port, elasticHost, elasticPort);
    }


    /**
     * Parses the Logs configuration file, and returns the an instance
     * that corresponds to the values in the configuration file.
     * @return
     */
    public static LogsConfiguration getLogsConfiguration() {
        JSONObject obj = parseConfigurationFile(LOGS_CONFIGURATION_FILE_PATH);
        String logMessage = (String) obj.get("logMessage");
        return new LogsConfiguration(logMessage);
    }


    /**
     * A method to parse the Server Configuration file written in JSON format. We store the
     * JSON parameters as private members in the class.
     *
     * @param configurationFilePath - the path to the server configuration file
     */
    private static JSONObject parseConfigurationFile(String configurationFilePath) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(configurationFilePath));
            return (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

class ServerConfiguration {
    private int port;
    private int elasticPort;
    private String elasticHost;

    public ServerConfiguration(int port, String elasticHost, int elasticPort) {
        this.port = port;
        this.elasticHost = elasticHost;
        this.elasticPort = elasticPort;
    }

    public int getPort() { return this.port; }
    public String getElasticHost() { return this.elasticHost; }
    public int getElasticPort() { return this.elasticPort; }
}

class LogsConfiguration {
    private String logMessage;

    public LogsConfiguration(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() { return this.logMessage; }
}
