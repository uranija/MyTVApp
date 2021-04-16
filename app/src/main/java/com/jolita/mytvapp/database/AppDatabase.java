package com.jolita.mytvapp.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



    @Database(entities = {FavoriteEntry.class}, version = 1, exportSchema = false)

    public abstract class AppDatabase extends RoomDatabase {

        private static final String LOG_TAG = AppDatabase.class.getSimpleName();
        private static final Object LOCK = new Object();
        private static final String DATABASE_NAME = "favorite";
        private static AppDatabase sInstance;

        public static AppDatabase getInstance(Context context) {
            if (sInstance == null) {
                synchronized (LOCK) {
                    Log.d(LOG_TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME)
                            // TODO (1) Disable queries on main thread
                            // Queries should be done in a separate thread to avoid locking the UI
                            // We will allow this ONLY TEMPORALLY to see that our DB is working

                            .build();
                }
            }
            Log.d(LOG_TAG, "Getting the database instance");
            return sInstance;
        }

        public abstract FavoriteDao favoriteDao();

    }




