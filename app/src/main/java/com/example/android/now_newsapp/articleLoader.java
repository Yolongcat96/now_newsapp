package com.example.android.now_newsapp;

import android.content.Context;
//import android.support.v4.content.AsyncTaskLoader;
import java.util.List;
import android.content.AsyncTaskLoader;

public class articleLoader extends AsyncTaskLoader<List<article>> {

    // Tag for log messages
    private static final String LOG_TAG = articleLoader.class.getName();

    // Query URL
    private String mUrl;

    public articleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // This is a background thread
    @Override
    public List<article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of articles.
        List<article> articles = QueryUtils.fetchArticleData(mUrl);

        return articles;
    }
}
