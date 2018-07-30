package com.example.android.now_newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.Toolbar;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String api_key = "4ab38b9a-63a4-42a3-86c4-b34de70d93f6";
    private static final int ARTICLE_LOADER_ID = 0;
    public static final String LOG_TAG = ArticleActivity.class.getName();

    private TextView mEmptyStateTextView;
    private String currCategoryID = "";

    // Adapter for the list of articles
    private ArticleAdapter mAdapter;

    private static final String URL_REQUEST = "https://content.guardianapis.com/search?";
    private static final String AND_OPERATOR = "%20AND%20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        // get the chosen category value from the previous activity
        currCategoryID = getIntent().getStringExtra("categoryID");
        // set the title
        setTitle();
        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);
        // Create a new {@link ArrayAdapter} of articles
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);
        // Set a click listener to play the audio when the list item is clicked on
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Article currentArticle = mAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentArticle.getaWebUrl());
                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });
        // Get a reference to the connectivityManager to check the state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    // Set the title based on the current category
    private void setTitle() {
        getSupportActionBar().setTitle(currCategoryID.toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this prefrenece.
        String numOfDisaplayedNews = sharedPrefs.getString(
                getString(R.string.settings_min_num_displayed_key),
                getString(R.string.settings_min_num_displayed_default));

        String orderByValue = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // include all categories
        StringBuilder themes = new StringBuilder();
        themes.append(getString(R.string.technology_id) + AND_OPERATOR);
        themes.append(getString(R.string.education_id)  + AND_OPERATOR);
        themes.append(getString(R.string.sports_id)      + AND_OPERATOR);
        themes.append(getString(R.string.politics_id)   + AND_OPERATOR);

        if (themes.toString().endsWith(AND_OPERATOR)) {
            themes.delete(themes.toString().length() - AND_OPERATOR.length(), themes.toString().length());
            Log.d(LOG_TAG, themes.toString());
        }

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(URL_REQUEST);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", themes.toString());
        uriBuilder.appendQueryParameter("section", currCategoryID);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", numOfDisaplayedNews);
        uriBuilder.appendQueryParameter("order-by", orderByValue); // relevance
        uriBuilder.appendQueryParameter("api-key", api_key);
        Log.d(LOG_TAG, uriBuilder.toString());

        // Create a new loader for the given URL
        return new ArticleLoader(ArticleActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Hide loading indicator because the data has been loaded.
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No article found."
        mEmptyStateTextView.setText(R.string.no_article);
        // Clear the adapter of previous article data
        mAdapter.clear();
        // If there is a valid list of {@link article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
