package com.example.android.now_newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class MainViewActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainViewActivity.class.getName();

    final int numberOfCategory = 4;
    static final int[] categoryLayoutID;
    static {
        categoryLayoutID = new int[4];
        categoryLayoutID[0]=R.id.technologyLayout;
        categoryLayoutID[1]=R.id.educationLayout;
        categoryLayoutID[2]=R.id.sportsLayout;
        categoryLayoutID[3]=R.id.politicsLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the title
        setTitle();
        setContentView(R.layout.activity_main_view);
        // Set touch event to all category boxex
        setTouchCategoryBoxes();
    }

    private void setTitle() {
        getSupportActionBar().setTitle(getResources().getString(R.string.category_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // Related to icons on the toolbar: menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private String getCategoryNameWithIndex(int index) {

        String chosenCategoryID = "";

        switch (index) {
            case 0:
                chosenCategoryID = getResources().getString(R.string.technology_id);
                break;
            case 1:
                chosenCategoryID = getResources().getString(R.string.education_id);
                break;
            case 2:
                chosenCategoryID = getResources().getString(R.string.sports_id);
                break;
            case 3:
                chosenCategoryID = getResources().getString(R.string.politics_id);
                break;
            default:
                chosenCategoryID = getResources().getString(R.string.technology_id);
                break;
        }

        return chosenCategoryID;
    }

    public void setTouchCategoryBoxes() {

        for (int index=0; index < numberOfCategory ; index++) {

            final RelativeLayout currRL = (RelativeLayout) findViewById(categoryLayoutID[index]);
            currRL.setTag(index);

            currRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent articleViewIntent = new Intent(getApplicationContext(), ArticleActivity.class);
                    articleViewIntent.putExtra("categoryID", getCategoryNameWithIndex((Integer) currRL.getTag()));
                    startActivity(articleViewIntent);
                }
            });

        }

    }

}
