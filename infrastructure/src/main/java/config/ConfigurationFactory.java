package config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

public class ConfigurationFactory {

    /**
     *
     * @param clazz
     * @param <T>
     * @return
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


    public static void main(String[] args) {

    }
}

