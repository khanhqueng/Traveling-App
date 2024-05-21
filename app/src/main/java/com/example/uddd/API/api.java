package com.example.uddd.API;

import com.example.uddd.Models.PlacesInfo;
import com.example.uddd.Models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface api {
    @FormUrlEncoded
    @POST("/api/createUser")
    Call<User> createUser(
            @Field("Hoten") String Hoten,
            @Field("Email") String Email,
            @Field("Password") String Password
    );
    @GET("/api/userLogin")
    Call<User> LoginUser(
            @Query("email") String email,
            @Query("pass") String pass
    );

    @GET("/search/searchbox/v1/category/coffee")
    Call<PlacesInfo> GetNearby(
            @Query("access_token") String access_token,
            @Query("language") String language,
            @Query("limit") int limit,
            @Query("proximity") String proximity
    );
    @GET("/search/geocode/v6/forward")
    Call<PlacesInfo> geocode(
            @Query("q") String placeName,
            @Query("access_token") String access_token
    );


}
