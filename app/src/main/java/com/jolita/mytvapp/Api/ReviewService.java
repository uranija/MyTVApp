package com.jolita.mytvapp.Api;

import com.jolita.mytvapp.model.ReviewResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {
    //Reviews
    @GET("tv/{tv_id}/reviews")
    Call<ReviewResult> getReview(@Path("tv_id") int id, @Query("api_key") String apiKey);
}
