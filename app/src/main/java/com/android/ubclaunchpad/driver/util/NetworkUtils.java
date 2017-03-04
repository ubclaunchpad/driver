package com.android.ubclaunchpad.driver.util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kelvinchan on 2017-03-04.
 */

public class NetworkUtils {
    private final OkHttpClient client = new OkHttpClient(); //should be singleton


    public Response GetRequestSync(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new IOException("Unexpected response" + response);
        }
        else{
            return response;
        }
    }

    public void GetRequestAsync(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
