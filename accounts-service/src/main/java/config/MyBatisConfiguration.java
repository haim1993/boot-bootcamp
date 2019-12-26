package config;

import java.util.Map;

public class MyBatisConfiguration {

    public final static String MYBATIS_ENVIRONMENT_ID   = "mybatis.environment.id";
    public final static String JDBC_HOST                = "JDBC.host";
    public final static String JDBC_PORT                = "JDBC.port";
    public final static String JDBC_SCHEMA              = "JDBC.schema";
    public final static String JDBC_USERNAME            = "JDBC.username";
    public final static String JDBC_PASSWORD            = "JDBC.password";
    public final static String JDBC_AUTO_COMMIT         = "JDBC.autoCommit";

    private String environmentId;
    private String host;
    private String port;
    private String schema;
    private String username;
    private String password;
    private String autoCommit;

    public MyBatisConfiguration(Map<String, Object> jsonMap) {
        this.environmentId = (String) jsonMap.get(MYBATIS_ENVIRONMENT_ID);
        this.host = (String) jsonMap.get(JDBC_HOST);
        this.port = (String) jsonMap.get(JDBC_PORT);
        this.schema = (String) jsonMap.get(JDBC_SCHEMA);
        this.username = (String) jsonMap.get(JDBC_USERNAME);
        this.password = (String) jsonMap.get(JDBC_PASSWORD);
        this.autoCommit = (String) jsonMap.get(JDBC_AUTO_COMMIT);
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getSchema() {
        return schema;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String isAutoCommit() {
        return autoCommit;
    }
}
