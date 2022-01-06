package module.api;

import java.io.IOException;

import okhttp3.*;

public class Http {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String API_W = "eb0dfd4f-cdc9-4546-9296-5bcdd69767e6";
    public static final String BASE_URL = "http://api-simulation:3000/api/";
    final OkHttpClient client = new OkHttpClient();

    /** Instance unique non préinitialisée */
    private static Http INSTANCE = null;

    /** Constructeur privé */
    Http()
    {}
     
    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized Http getInstance()
    {           
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
