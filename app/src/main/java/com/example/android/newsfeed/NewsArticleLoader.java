package com.example.android.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of books by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsArticleLoader extends AsyncTaskLoader<List<NewsArticle>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsArticle.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link NewsArticleLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v("Loader", "startLoading");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsArticle> loadInBackground() {
        Log.v("Loader", "loadInBackground");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        List<NewsArticle> news = QueryUtils.fetchNewsArticleData(mUrl);
        return news;
    }
}
