package app;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesReader {

    public Properties getProp() {
        return prop;
    }

    private Properties prop;

    public PropertiesReader() throws Exception {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "config.properties";

        Properties prop = new Properties();
        prop.load(new FileInputStream(appConfigPath));
        this.prop = prop;
    }
}
