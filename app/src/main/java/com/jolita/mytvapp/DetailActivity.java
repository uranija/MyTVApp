package com.jolita.mytvapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.jolita.mytvapp.Api.Client;
import com.jolita.mytvapp.Api.ReviewService;
import com.jolita.mytvapp.Api.SimilarTVService;
import com.jolita.mytvapp.Api.VideoService;
import com.jolita.mytvapp.adapter.ReviewAdapter;
import com.jolita.mytvapp.adapter.SimilarTVAdapter;
import com.jolita.mytvapp.adapter.VideoAdapter;
import com.jolita.mytvapp.database.AppDatabase;
import com.jolita.mytvapp.database.FavoriteEntry;
import com.jolita.mytvapp.model.Review;
import com.jolita.mytvapp.model.ReviewResult;
import com.jolita.mytvapp.model.SimilarTV;
import com.jolita.mytvapp.model.SimilarTVResult;
import com.jolita.mytvapp.model.TV;
import com.jolita.mytvapp.model.Video;
import com.jolita.mytvapp.model.VideoResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jolita.mytvapp.BuildConfig.API_KEY;

public class DetailActivity extends AppCompatActivity {
    TextView tvName, plotOverview, userRating, releaseDate;
    ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView3;
    private VideoAdapter videoAdapter;
    private SimilarTVAdapter similarTVAdapter;
    private RecyclerView recyclerView2;
    private ReviewAdapter reviewAdapter;
    private List<Video> videoList;
    private List<SimilarTV> similarTVList;
    private List<Review> reviewList;
    TV tv;
    String tvPoster, tvTitle, overview, rating, dateOfRelease;
    int tv_id;
    // Member variable for the Database
    private AppDatabase mDb;
    private AdView mAdView;
    List<FavoriteEntry> entries = new ArrayList<>();
    private final AppCompatActivity activity = DetailActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDb = AppDatabase.getInstance(getApplicationContext());

        imageView = (ImageView) findViewById(R.id.thumbnail_image);
        tvName = (TextView) findViewById(R.id.title);
        plotOverview = (TextView) findViewById(R.id.plotsynopsis);
        userRating = (TextView) findViewById(R.id.userrating);
        releaseDate = (TextView) findViewById(R.id.releasedate);

        /*
         * Here is where all the magic happens. The getIntent method will give us the Intent that
         * started this particular Activity.
         */

        Intent intentThatStartedThisActivity = getIntent();

        /*
         * Although there is always an Intent that starts any particular Activity, we can't
         * guarantee that the extra we are looking for was passed as well. Because of that, we need
         * to check to see if the Intent has the extra that we specified when we created the
         * Intent that we use to start this Activity. Note that this extra may not be present in
         * the Intent if this Activity was started by any other method.
         * */

        if (intentThatStartedThisActivity.hasExtra("TV")) {

            /*
             * Now that we've checked to make sure the extra we are looking for is contained within
             * the Intent, we can extract the extra. To do that, we simply call the getStringExtra
             * method on the Intent. There are various other get*Extra methods you can call for
             * different types of data. Please feel free to explore those yourself.
             */
            tv = getIntent().getParcelableExtra("TV");

            tvPoster = tv.getPosterPath();
            tvTitle = tv.getOriginalTitle();
            overview = tv.getOverview();
            rating = Double.toString(tv.getVoteAverage());
            dateOfRelease = tv.getReleaseDate();
            tv_id = tv.getId();


            /*
             * Finally, we can set the text of our TextView (using setText) to the text that was
             * passed to this Activity.
             *
             */
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + tvPoster)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.load)
                    .into(imageView);


            tvName.setText(tvTitle);
            plotOverview.setText(overview);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);

        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
        initVideos();
        checkStatus(tvTitle);
        initReviews();
        initSimilarTV();
    }

    private void initVideos() {
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(this, videoList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();

        loadMovieVideos();
        //loadReviews();
    }

    private void initReviews() {
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.setAdapter(reviewAdapter);
        videoAdapter.notifyDataSetChanged();


        loadReviews();
    }


    private void loadMovieVideos() {

        try {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Get your API Key ", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            VideoService apiService = Client.getClient().create(VideoService.class);
            Call<VideoResult> call = apiService.getMovieVideos(tv_id, API_KEY);
            call.enqueue(new Callback<VideoResult>() {
                @Override
                public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
                    List<Video> videos = response.body().getResults();
                    recyclerView.setAdapter(new VideoAdapter(getApplicationContext(), videos));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<VideoResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadReviews() {

        try {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Get your API Key", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            ReviewService apiService = Client.getClient().create(ReviewService.class);
            Call<ReviewResult> call = apiService.getReview(tv_id, API_KEY);
            call.enqueue(new Callback<ReviewResult>() {
                @Override
                public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                    List<Review> reviews = response.body().getResults();
                    recyclerView2.setAdapter(new ReviewAdapter(getApplicationContext(), reviews));
                    recyclerView2.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<ReviewResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initSimilarTV() {
        similarTVList = new ArrayList<>();
        similarTVAdapter = new SimilarTVAdapter(this, similarTVList);

        recyclerView3 = (RecyclerView) findViewById(R.id.recycler_view3);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView3.setLayoutManager(mLayoutManager);
        recyclerView3.setAdapter(similarTVAdapter);
        similarTVAdapter.notifyDataSetChanged();


        loadSimilarTV();
    }

    private void loadSimilarTV() {

        try {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Get your API Key", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            SimilarTVService apiService = Client.getClient().create(SimilarTVService.class);
            Call<SimilarTVResult> call = apiService.getSimilarTV(tv_id, API_KEY);
            call.enqueue(new Callback<SimilarTVResult>() {
                @Override
                public void onResponse(Call<SimilarTVResult> call, Response<SimilarTVResult> response) {
                    List<SimilarTV> similartv = response.body().getResults();
                    recyclerView3.setAdapter(new SimilarTVAdapter(getApplicationContext(), similartv));
                    recyclerView3.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<SimilarTVResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Was using this tutorial to implement favorite tv option with room database :
     * "https://github.com/delaroy/MoviesApp/blob/room/app/src/main/java/com/delaroystudios/movieapp/DetailActivity.java"
     **/
    public void saveFavoriteTV() {
        Double rate = tv.getVoteAverage();
        final FavoriteEntry favoriteEntry = new FavoriteEntry(tv_id, tvPoster, tvTitle, rate, dateOfRelease, overview);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().insertFavorite(favoriteEntry);
            }
        });
    }

    private void deleteFavoriteTV(final int tv_id) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().deleteFavoriteWithId(tv_id);
            }
        });
    }


    // Using material favorite button to favorite movies from https://github.com/IvBaranov/MaterialFavoriteButton //
    @SuppressLint("StaticFieldLeak")
    private void checkStatus(final String tvName) {
        final MaterialFavoriteButton materialFavoriteButton = (MaterialFavoriteButton) findViewById(R.id.favorite_button);



        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                entries.clear();
                entries = mDb.favoriteDao().loadAll(tvName);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (entries.size() > 0) {
                    materialFavoriteButton.setFavorite(true);
                    materialFavoriteButton.setOnFavoriteChangeListener(
                            new MaterialFavoriteButton.OnFavoriteChangeListener() {
                                @Override
                                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                                    if (favorite == true) {
                                        saveFavoriteTV();
                                        materialFavoriteButton.setContentDescription("Add to Favorite");
                                        Snackbar.make(buttonView, "Added to Favorite",
                                                Snackbar.LENGTH_SHORT).show();

                                    } else {
                                        deleteFavoriteTV(tv_id);
                                        materialFavoriteButton.setContentDescription("Remove from favorite");
                                        Snackbar.make(buttonView, "Removed from Favorite",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });


                } else {
                    materialFavoriteButton.setOnFavoriteChangeListener(
                            new MaterialFavoriteButton.OnFavoriteChangeListener() {
                                @Override
                                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                                    if (favorite == true) {
                                        saveFavoriteTV();
                                        materialFavoriteButton.setContentDescription("Add to Favorite");
                                        Snackbar.make(buttonView, "Added to Favorite",
                                                Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        int tv_id = getIntent().getExtras().getInt("id");
                                        deleteFavoriteTV(tv_id);
                                        materialFavoriteButton.setContentDescription("Remove from favorite");
                                        Snackbar.make(buttonView, "Removed from Favorite",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        }.execute();
    }


}
