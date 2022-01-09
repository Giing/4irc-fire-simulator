package module.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import app.PropertiesReader;
import okhttp3.*;

public class Http {
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String API_W;
    private String BASE_URL;
    final OkHttpClient client = new OkHttpClient();


    public Http(String baseUrl, String tokenApi)  {
        BASE_URL = "http://" + baseUrl;
        System.out.println(BASE_URL);
        API_W = tokenApi;
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

    public String delete(String url) throws IOException {
        Request request = new Request.Builder()
            .url(BASE_URL + url)
            .header("x-access-token", API_W)
            .delete()
            .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
