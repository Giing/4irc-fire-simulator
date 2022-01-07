package app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    public Properties getProp() {
        return prop;
    }

    private Properties prop;

    public PropertiesReader() throws Exception {
        InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
        

        Properties prop = new Properties();
        prop.load(config);
        this.prop = prop;
    }
}
