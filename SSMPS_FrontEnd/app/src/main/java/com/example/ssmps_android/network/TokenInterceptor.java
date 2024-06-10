package com.example.ssmps_android.network;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private String token;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Headers.Builder headersBuilder = request.headers().newBuilder();

        if(request.url().toString().contains("/manager")){
            headersBuilder.add("Authorization", token);
        }

        Request newReq = chain.request().newBuilder()
                .headers(headersBuilder.build())
                .build();
        return chain.proceed(newReq);
    }

    public void setToken(String token) {
        this.token = token;
    }
}
