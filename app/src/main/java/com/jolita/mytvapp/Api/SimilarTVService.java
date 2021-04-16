package com.jolita.mytvapp.Api;

import com.jolita.mytvapp.model.SimilarTVResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SimilarTVService {
    //Reviews
    @GET("tv/{tv_id}/similar")
    Call<SimilarTVResult> getSimilarTV(@Path("tv_id") int id, @Query("api_key") String apiKey);
}
