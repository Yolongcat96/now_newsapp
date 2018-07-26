package com.example.android.now_newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class articleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<article>> {

    private static final String api_key = "4ab38b9a-63a4-42a3-86c4-b34de70d93f6";
    private static final int ARTICLE_LOADER_ID = 0;
    public static final String LOG_TAG = articleActivity.class.getName();

    private TextView mEmptyStateTextView;
    private String currCategoryID = "";

    // Adapter for the list of articles
    private articleAdapter mAdapter;

    private static final String URL_REQUEST = "https://content.guardianapis.com/search?";
    private static final String AND_OPERATOR = "%20AND%20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        // get the chosen category value from the previous activity
        currCategoryID = getIntent().getStringExtra("categoryID");
        // set category title view
        TextView categoryTitleView = (TextView)findViewById(R.id.categoryTextView);
        categoryTitleView.setText(currCategoryID);
        // set goback funtion
        setBackArrowFunction();
        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);
        // Create a new {@link ArrayAdapter} of articles
        //itemAdapter adapter = new itemAdapter(this, articles);
        mAdapter = new articleAdapter(this, new ArrayList<article>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);
        // Set a click listener to play the audio when the list item is clicked on
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                article currentArticle = mAdapter.getItem(position);
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


    // When clicking the backarrow button, it moves the user to the previous (main List) activity.
    public void setBackArrowFunction() {
        final ImageView backArrowIV = (ImageView) findViewById(R.id.backArrow);
        backArrowIV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mainViewIntent = new Intent(getApplicationContext(), mainViewActivity.class);
                startActivity(mainViewIntent);
            }
        });
    }

    @Override
    public Loader<List<article>> onCreateLoader(int i, Bundle bundle) {

        // include all categories
        StringBuilder themes = new StringBuilder();
        themes.append(getString(R.string.technology_id) + AND_OPERATOR);
        themes.append(getString(R.string.education_id)  + AND_OPERATOR);
        themes.append(getString(R.string.sports_id)     + AND_OPERATOR);
        themes.append(getString(R.string.politics_id)   + AND_OPERATOR);

        if (themes.toString().endsWith(AND_OPERATOR)) {
            themes.delete(themes.toString().length() - AND_OPERATOR.length(), themes.toString().length());
            Log.d(LOG_TAG, themes.toString());
        }

        Uri baseUri = Uri.parse(URL_REQUEST);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", themes.toString());
        uriBuilder.appendQueryParameter("section", currCategoryID);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("order-by", "relevance");
        uriBuilder.appendQueryParameter("api-key", api_key);
        Log.d(LOG_TAG, uriBuilder.toString());

        // Create a new loader for the given URL
        return new articleLoader(articleActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<article>> loader, List<article> articles) {
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
    public void onLoaderReset(Loader<List<article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
