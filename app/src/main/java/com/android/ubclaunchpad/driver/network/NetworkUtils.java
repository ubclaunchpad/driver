package com.android.ubclaunchpad.driver.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kelvinchan on 2017-03-04.
 * <p>
 * Useful resources:
 * https://github.com/square/okhttp/wiki/Recipes
 * http://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html
 * <p>
 * OKHttp based on:
 * https://github.com/square/okhttp
 */

public class NetworkUtils {
    private NetworkUtils networkUtils;
    private OkHttpClient client;

    public NetworkUtils() {
        client = new OkHttpClient();
    }

    public NetworkUtils getInstance() {
        if (networkUtils == null) {
            networkUtils = new NetworkUtils();
        }
        return networkUtils;
    }

    /**
     * A synchronous way of making a GET request
     *
     * @param url the url you are trying to get
     * @return Response from the server. Can use to get header and body
     * @throws IOException
     */
    public Response GetRequestSync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response" + response);
        } else {
            return response;
        }
    }

    /**
     * An async way of making a GET request. Handles return in callback
     *
     * @param url      the url you are trying to GET from
     * @param callback a callback to handle the return from the request
     */
    public void GetRequestAsync(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * Creates a new client with specified timeouts
     *
     * @param connTimeoutMs connection timeout in milliseconds
     * @param readTimeoutMs read timeout in milliseconds
     */
    public void ConfigureClient(int connTimeoutMs, int readTimeoutMs) {
        client = new OkHttpClient.Builder()
                .connectTimeout(connTimeoutMs, TimeUnit.SECONDS)
                .readTimeout(readTimeoutMs, TimeUnit.SECONDS).build();
    }
}
