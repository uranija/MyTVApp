package com.jolita.mytvapp.Api;

import com.jolita.mytvapp.model.TVResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TVService {
    @GET("tv/popular")
    Call<TVResult> getPopularTV(@Query("api_key") String apiKey);


    @GET("tv/airing_today")
    Call<TVResult> getAiringToday(@Query("api_key") String apiKey);

}
