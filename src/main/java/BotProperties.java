import java.io.IOException;
import java.util.Properties;

public class BotProperties {

    private static BotProperties instance = null;
    private Properties properties;

    protected BotProperties() throws IOException {
        properties = new Properties();
        properties.load(getClass().getResourceAsStream("data.properties"));
    }

    public static BotProperties getInstance(){
        if(instance == null){
            try {
                instance = new BotProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public String getValue(String key){
        return properties.getProperty(key);
    }
}
