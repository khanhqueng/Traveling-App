package com.example.uddd.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String URL="http://10.0.2.2:8090";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private RetrofitClient(){
        retrofit= new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static synchronized RetrofitClient getInstance(){
        if(mInstance==null){
            mInstance= new RetrofitClient();

        }
        return mInstance;
    }
    public api getAPI(){
        return retrofit.create(api.class);
    }
}
