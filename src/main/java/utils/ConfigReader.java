package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read and validate framework configuration parameters.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file '" + CONFIG_FILE + "' not found on the classpath.");
            }
            PROPERTIES.load(inputStream);
            validateRequiredKeys();
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load configuration properties from '" + CONFIG_FILE + "': " + e.getMessage());
        }
    }

    private ConfigReader() {
        // Prevent direct instantiation of utility class
    }

    private static void validateRequiredKeys() {
        String[] requiredKeys = {"browser", "headless", "url", "explicitWaitSeconds"};
        for (String key : requiredKeys) {
            String value = getProperty(key);
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalStateException("Mandatory configuration property missing or empty: '" + key + "'");
            }
        }
    }

    /**
     * Retrieves the configured target browser, checking system properties first before properties file.
     *
     * @return the browser name in lowercase
     */
    public static String getBrowser() {
        return getProperty("browser").trim().toLowerCase();
    }

    /**
     * Determines whether headless execution mode is enabled via system property or configuration.
     *
     * @return true if headless execution is requested, false otherwise
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless").trim());
    }

    /**
     * Retrieves the base URL of the target application.
     *
     * @return the application URL string
     */
    public static String getUrl() {
        return getProperty("url").trim();
    }

    /**
     * Retrieves the explicit wait duration in seconds for WebDriver synchronization.
     *
     * @return explicit wait duration in seconds
     */
    public static int getExplicitWaitSeconds() {
        try {
            return Integer.parseInt(getProperty("explicitWaitSeconds").trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Property 'explicitWaitSeconds' must be a valid integer: " + e.getMessage());
        }
    }

    /**
     * Retrieves a configuration property value with system property precedence.
     *
     * @param key the property key to look up
     * @return the resolved property value, or null if not found
     */
    public static String getProperty(String key) {
        String systemVal = System.getProperty(key);
        if (systemVal != null && !systemVal.trim().isEmpty()) {
            return systemVal;
        }
        return PROPERTIES.getProperty(key);
    }
}
