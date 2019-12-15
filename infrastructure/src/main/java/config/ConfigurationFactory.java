package config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

public class ConfigurationFactory {

    /**
     * Method to load some configuration file URL, with a corresponding class to inject data into.
     *
     * @param fileUrl - configuration file URL
     * @param clazz - Object class
     * @param <T> - generic class
     * @return A loaded object with the configurations
     */
    public <T> T load(String fileUrl, Class<T> clazz) {
        try {
            Map<String, Object> jsonMap = getJsonMap(fileUrl);
            Constructor<T> ctor = clazz.getConstructor(Map.class);
            return ctor.newInstance(new Object[] { jsonMap });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Method to get Map object with configurations from given 'fileUrl' path.
     *
     * @return
     * @throws IOException
     */
    private Map<String, Object> getJsonMap(String fileUrl) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(
                new File(fileUrl),
                new TypeReference<Map<String, Object>>() {});
    }

}

