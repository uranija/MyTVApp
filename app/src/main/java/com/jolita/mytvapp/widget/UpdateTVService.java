package com.jolita.mytvapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class UpdateTVService extends IntentService {
    public static String REVIEWS_LIST = "REVIEWS_LIST";
    public static String REVIEWS_LIST2 = "REVIEWS_LIST2";

    public UpdateTVService() {
        super("UpdateTVService");
    }

    public static void startBakingService(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, UpdateTVService.class);
        intent.putExtra(REVIEWS_LIST, ingredientsList);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> reviewsList = intent.getExtras().getStringArrayList(REVIEWS_LIST);
            ArrayList<String> reviews2List = intent.getExtras().getStringArrayList(REVIEWS_LIST2);

            handleActionUpdateBakingWidgets(reviewsList, reviews2List);

        }
    }


    private void handleActionUpdateBakingWidgets(ArrayList<String> reviewsList, ArrayList<String> reviews2List) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(REVIEWS_LIST, reviewsList);
        intent.putExtra(REVIEWS_LIST2, reviews2List);

        sendBroadcast(intent);
    }
}
