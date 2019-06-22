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

        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.get("application/json"), "{ \"stationId\": \"" + stationId + "\" }"))
                .build();

        return sendRequest(request);
    }

    public static String finishSession(final String url) {
        log.info("Finishing session in service: {}", url);

        final Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.get("application/json"), "{}"))
                .build();

        return sendRequest(request);
    }

    private static String sendRequest(final Request request) {
        try (final Response response = new OkHttpClient().newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            final String message = String.format("Failed to send request: %s", request.url());
            log.error(message, e);
            return "{\"error\": \"" + message + "\"}";
        }
    }
}