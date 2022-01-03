package app;

import java.io.IOException;

import okhttp3.*;

public class API {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String API_W = "eb0dfd4f-cdc9-4546-9296-5bcdd69767e6";
    public static final String BASE_URL = "http://localhost:3000/api/";
    final OkHttpClient client = new OkHttpClient();

    String get(String url) throws IOException {
        Request request = new Request.Builder()
            .url(BASE_URL + url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String post(String url, String json) throws IOException {
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
