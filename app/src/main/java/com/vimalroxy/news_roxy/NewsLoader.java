package com.vimalroxy.news_roxy;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    public NewsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        List<News> listOfNews = null;
        try {
            URL url = Utils.createUrl();
            String jsonResponse = Utils.makeHttpRequest(url);
            listOfNews = Utils.extractNewsJSON(jsonResponse);
        } catch (IOException e) {
            Log.e("UTILS", "Error Loader LoadInBackground: ", e);
        }
        return listOfNews;
    }
}
