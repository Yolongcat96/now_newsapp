package com.example.android.now_newsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class mainViewActivity extends AppCompatActivity {

    public static final String LOG_TAG = mainViewActivity.class.getName();

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
        setContentView(R.layout.activity_main_view);
        // set go back image
        setBackArrowFunction();
        // Set touch event to all category boxex
        setTouchCategoryBoxes();
    }

    // When clicking the backarrow button, it moves the user to the previous (main List) activity.
    public void setBackArrowFunction() {
        final ImageView backArrowIV = (ImageView) findViewById(R.id.backArrow);
        backArrowIV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startViewIntent = new Intent(getApplicationContext(), startActivity.class);
                startActivity(startViewIntent);
            }
        });
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

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchCategoryBoxes() {

        for (int index=0; index < numberOfCategory ; index++) {
            final RelativeLayout currRL = (RelativeLayout) findViewById(categoryLayoutID[index]);
            currRL.setTag(index);
            currRL.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    Log.d(LOG_TAG,"Category Tag: " + currRL.getTag());
                    Intent articleViewIntent = new Intent(getApplicationContext(), articleActivity.class);
                    articleViewIntent.putExtra("categoryID", getCategoryNameWithIndex((Integer) currRL.getTag()));
                    startActivity(articleViewIntent);

                    return true;//always return true to consume event
                }
            });
        }

    }
}
