package core.utils;

import java.io.IOException;
import java.util.Properties;


public class PropertyLoader {

    private static final String PROP_FILE = "/application.properties";

    private static Properties properties;

    public static synchronized String getProperty (String propertyName) {
        if (properties == null) {
            loadProperty ();
        }
        String value = properties.getProperty(propertyName);
        if (value != null) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Property <%s> is not found in file <%s>",propertyName, PROP_FILE));
        }
    }

    private static void loadProperty () {
        properties = new Properties ();
        try {
            properties.load (PropertyLoader.class.getResourceAsStream(PROP_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}