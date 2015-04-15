package ciber.calendar.util;

import java.util.Properties;

public class PropertyHolder {

    private static final Properties properties = new Properties();

    public static void setup() {
        try {
            properties.load(PropertyHolder.class.getClassLoader().getResourceAsStream("router.properties"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        else {
            throw new RuntimeException("Missing required property: " + key);
        }
    }

}
