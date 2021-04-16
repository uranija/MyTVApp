package com.jolita.mytvapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jolita.mytvapp.Api.Client;
import com.jolita.mytvapp.Api.TVService;
import com.jolita.mytvapp.adapter.TVAdapter;
import com.jolita.mytvapp.database.AppDatabase;
import com.jolita.mytvapp.database.FavoriteEntry;
import com.jolita.mytvapp.model.TV;
import com.jolita.mytvapp.model.TVResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    /**
     * API_KEY is left here for debugging, will delete it before uploading to my github repo
     **/
    //public static final String API_KEY = "Myapikey";
    String API_KEY = BuildConfig.API_KEY;
    private AdView mAdView;
    private RecyclerView recyclerView;
    private List<TV> tvList;
    private TVAdapter adapter;
    ProgressDialog pd;
    
    public static final String LOG_TAG = TVAdapter.class.getName();
    // Member variable for the Database
    private AppDatabase mDb;
    public static final int ITEMS_PER_AD = 8;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        startPopularTV();
        startAiringToday();


    }

    
    private void startPopularTV() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        tvList = new ArrayList<>();
        adapter = new TVAdapter(this, tvList);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        sortTV();


    }

    /**
     * Method for getting information from the TMDB, will be using Retrofit library to fetch data.
     * Used this tutorial for getting TMDB data -
     * "https://www.supinfo.com/articles/single/7849-developing-popular-movies-application-in-android-using-retrofit"
     **/

    private void loadPopularTV() {

        try {

            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid API key", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;

            }
            Client Client = new Client();
            TVService apiTVService =
                    Client.getClient().create(TVService.class);
            Call<TVResult> call = apiTVService.getPopularTV(API_KEY);
            call.enqueue(new Callback<TVResult>() {
                @Override
                public void onResponse(Call<TVResult> call, Response<TVResult> response) {
                    List<TV> tv = response.body().getResults();
                    recyclerView.setAdapter(new TVAdapter(getApplicationContext(), tv));
                    recyclerView.smoothScrollToPosition(0);


                }

                @Override
                public void onFailure(Call<TVResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }

    }


    private void startAiringToday() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        tvList = new ArrayList<>();
        adapter = new TVAdapter(this, tvList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //loadTopRatedMovies();
        getAllFavorite();

    }


    private void loadAiringToday() {

        try {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid API key", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;

            }
            Client Client = new Client();
            TVService apiTVService =
                    Client.getClient().create(TVService.class);
            Call<TVResult> call = apiTVService.getAiringToday(API_KEY);
            call.enqueue(new Callback<TVResult>() {
                @Override
                public void onResponse(Call<TVResult> call, Response<TVResult> response) {
                    List<TV> tv = response.body().getResults();
                    recyclerView.setAdapter(new TVAdapter(getApplicationContext(), tv));
                    recyclerView.smoothScrollToPosition(0);


                }

                @Override
                public void onFailure(Call<TVResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }

    }


    /**
     * Methods for setting up the menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.tv_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }







    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG, "Preferences updated");
        sortTV();
    }

    private void sortTV() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortTV = preferences.getString(
                this.getString(R.string.pref_sort_tv_key),
                this.getString(R.string.pref_most_popular)
        );
        if (sortTV.equals(this.getString(R.string.pref_most_popular))) {
            Log.d(LOG_TAG, "Sorting by most popular");
            loadPopularTV();
        } else if (sortTV.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG, "Sorting by favorite");
            startAiringToday();
        } else {
            Log.d(LOG_TAG, "Sorting by vote average");
            loadAiringToday();
        }
    }

    @Override
    protected void onDestroy() {
        for (Object item : tvList) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.destroy();
            }
        }
        super.onDestroy();
        // Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onResume() {
        for (Object item : tvList) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.resume();
            }
        }
        super.onResume();
        if (tvList.isEmpty()) {
            sortTV();
        } else {

            sortTV();
        }
    }

    @Override
    protected void onPause() {
        for (Object item : tvList) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.pause();
            }
        }
        super.onPause();
    }


    /**
     * Was using this tutorial to implement favorite movie option with room database :
     * "https://github.com/delaroy/MoviesApp/blob/room/app/src/main/java/com/delaroystudios/movieapp/DetailActivity.java"
     **/
    private void getAllFavorite(){


        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorite().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> imageEntries) {
                List<TV> tvs = new ArrayList<>();
                for (FavoriteEntry entry : imageEntries){
                    TV tv = new TV();
                    tv.setId(entry.getTvid());
                    tv.setOverview(entry.getOverview());
                    tv.setOriginalTitle(entry.getTitle());
                    tv.setPosterPath(entry.getPosterpath());
                    tv.setVoteAverage(entry.getUserrating());
                    tv.setReleaseDate(entry.getReleasedate());
                    tvs.add(tv);
                }

                adapter.setTV(tvs);
            }
        });
    }





}