package com.jolita.mytvapp;

import com.jolita.mytvapp.database.AppDatabase;
import com.jolita.mytvapp.database.FavoriteEntry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddFavoriteViewModel extends ViewModel {
    // COMPLETED (6) Add a task member variable for the TaskEntry object wrapped in a LiveData
    private LiveData<FavoriteEntry> favorite;

    // COMPLETED (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public AddFavoriteViewModel(AppDatabase database, int favoriteId) {
        favorite = database.favoriteDao().loadFavoriteById(favoriteId);
    }

    // COMPLETED (7) Create a getter for the task variable
    public LiveData<FavoriteEntry> getFavorite() {
        return favorite;
    }
}
