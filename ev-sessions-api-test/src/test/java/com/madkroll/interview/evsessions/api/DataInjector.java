package com.madkroll.interview.evsessions.api;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DataInjector {

    private static final Logger log = LoggerFactory.getLogger(DataInjector.class);

    public static String newSession(final String url, final String stationId) {
        log.info("Adding new session at station {} to service: {}", stationId, url);

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.get("application/json"), "{ \"stationId\": \"" + stationId + "\" }"))
                .build();

        try (final Response response = client.newCall(request).execute()) {
            final String responseBody = response.body().string();
            log.info("Session has been created: {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            final String message = String.format("Failed to send request: %s", url);
            log.error(message, e);
            return "{\"error\": \"" + message + "\"}";
        }
    }
}