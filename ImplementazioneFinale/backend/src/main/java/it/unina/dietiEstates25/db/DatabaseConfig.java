package it.unina.dietiEstates25.db;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final String CONFIG="config.properties";

    private DatabaseConfig(){}

    public static String getDbUrl() {
        try {
            InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG);
            if (inputStream == null) {
                return "";
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty("db.url");
        }catch (Exception e){
            return "";
        }
    }

    public static String getClientSecret() {
        try {
            InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG);
            if (inputStream == null) {
                return "";
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty("client.secret");
        }catch (Exception e){
            return "";
        }
    }

    public static String getSecret() {
        try {
            InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG);
            if (inputStream == null) {
                return "";
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty("secret.key");
        }catch (Exception e){
            return "";
        }
    }



}
