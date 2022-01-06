package module.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import app.PropertiesReader;
import okhttp3.*;

public class Http {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static String API_W;
    public static String BASE_URL;
    final OkHttpClient client = new OkHttpClient();

    /** Instance unique non préinitialisée */
    private static Http INSTANCE = null;

    /** Constructeur privé */
    Http() throws Exception {
        PropertiesReader prop = new PropertiesReader();
        API_W = prop.getProp().getProperty("API_KEY");
        BASE_URL = prop.getProp().getProperty("BASE_URL");
    }
     
    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized Http getInstance() throws Exception {
        if (INSTANCE == null)
        {   INSTANCE = new Http(); 
        }
        return INSTANCE;
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
            .url(BASE_URL + url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(BASE_URL + url)
            .header("x-access-token", API_W)
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
