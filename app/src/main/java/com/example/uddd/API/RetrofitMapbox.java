package com.example.uddd.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMapbox {
    private static final String URL="https://api.mapbox.com";
    private static RetrofitMapbox mInstance;
    private Retrofit retrofit;
    private RetrofitMapbox(){
        retrofit= new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static synchronized RetrofitMapbox getInstance(){
        if(mInstance==null){
            mInstance= new RetrofitMapbox();

        }
        return mInstance;
    }
    public api getAPI(){
        return retrofit.create(api.class);
    }
}
