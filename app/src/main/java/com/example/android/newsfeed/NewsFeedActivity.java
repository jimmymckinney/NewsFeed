package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    public static final String LOG_TAG = NewsFeedActivity.class.getName();
    private static final int NEWS_LOADER_ID = 1;
    private String mRequestUrl;
    private NewsArticleAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private String mUserInput;
    private ProgressBar progress;
    private ListView newsListView;
    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                mEmptyStateTextView.setText(null); //Sets emptyText to null so that progress loader doesn't overlay emptyText
                mAdapter.clear(); //Clears adapter so progress loader doesn't overlay the last search results
                mRequestUrl = "https://content.guardianapis.com/search?q=";
                if (!checkNetworkActivity()) {
                    progress.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_connection);
                } else {


                    mUserInput = newText.replaceAll(" ", "%20");
                    mRequestUrl = "https://content.guardianapis.com/search?q=" + mUserInput + "&show-fields=thumbnail&api-key=test";

                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.restartLoader(NEWS_LOADER_ID, null, NewsFeedActivity.this);
                    Log.v("Context", mRequestUrl);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        // Find a reference to the {@link ListView} in the layout
        newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        progress = (ProgressBar) findViewById(R.id.progress_bar);
        if (!checkNetworkActivity()) {
            progress.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);
        } else {
            // Create a new {@link ArrayAdapter} of books
            mAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            newsListView.setAdapter(mAdapter);

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }

    }

    protected boolean checkNetworkActivity() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        progress.setVisibility(View.VISIBLE);
        Log.v("Loader", "onCreateLoader");
        // Create a new loader for the given URL
        return new NewsArticleLoader(this, mRequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
        progress.setVisibility(View.GONE);
        //bookListView.setVisibility(View.VISIBLE);

        //Set empty state text
        mEmptyStateTextView.setText(R.string.no_articles);

        if (mUserInput == null) {
            mEmptyStateTextView.setText(R.string.instructions);
        }

        Log.v("Loader", "onLoadFinished");
        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsArticles != null && !newsArticles.isEmpty()) {
            mAdapter.addAll(newsArticles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.v("Loader", "onLoaderReset");
        mAdapter.clear();
    }

}
