package com.jolita.mytvapp.Api;

import com.jolita.mytvapp.model.VideoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VideoService {

    @GET("tv/{tv_id}/videos")
    Call<VideoResult> getMovieVideos(@Path("tv_id") int id, @Query("api_key") String apiKey);
}
