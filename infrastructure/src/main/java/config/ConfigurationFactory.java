package config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;

public class ConfigurationFactory {

    /**
     * Method to load some configuration file URL, with a corresponding class to inject data into.
     *
     * @param filePath - configuration file URL
     * @param clazz - Object class
     * @param <T> - generic class
     * @return A loaded object with the configurations
     */
    public static <T> T load(String filePath, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            FileInputStream fileInputStream = new FileInputStream(filePath);
            return mapper.readValue(fileInputStream, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}

