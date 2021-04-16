package com.jolita.mytvapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResult implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Video> results = null;
    public final static Parcelable.Creator<VideoResult> CREATOR = new Creator<VideoResult>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VideoResult createFromParcel(Parcel in) {
            return new VideoResult(in);
        }

        public VideoResult[] newArray(int size) {
            return (new VideoResult[size]);
        }

    }
            ;

    protected VideoResult(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (com.jolita.mytvapp.model.Video.class.getClassLoader()));
    }

    public VideoResult() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}