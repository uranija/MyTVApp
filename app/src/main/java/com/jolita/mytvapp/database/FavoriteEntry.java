package com.jolita.mytvapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class FavoriteEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "tvid")
    private int tvid;

    @ColumnInfo(name = "posterpath")
    private String posterpath;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "userrating")
    private Double userrating;

    @ColumnInfo(name = "releasedate")
    private String releasedate;

    @ColumnInfo(name = "overview")
    private String overview;


    @Ignore
    public FavoriteEntry(int tvid, String posterpath, String title,Double userrating,String releasedate,String overview ) {
        this.tvid = tvid;
        this.posterpath = posterpath;
        this.title = title;
        this.userrating=userrating;
        this.releasedate=releasedate;
        this.overview=overview;
    }

    public FavoriteEntry(int id,int tvid, String posterpath, String title,Double userrating, String releasedate, String overview ) {
        this.id = id;
        this.tvid = tvid;
        this.posterpath = posterpath;
        this.title = title;
        this.userrating=userrating;
        this.releasedate=releasedate;
        this.overview=overview;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTvid() {
        return tvid;
    }

    public void setTVid(int tvid) {
        this.tvid = tvid;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getUserrating() {
        return userrating;
    }

    public void setUserrating(double userrating) {
        this.userrating = userrating;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


}






