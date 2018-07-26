package com.example.android.now_newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Set the click event on the START button
        setClickEventOnStartButton();
    }

    // When clicking the START button, the user can go to the main view (main news view)
    private void setClickEventOnStartButton() {

        final Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //call main view activity
                Intent mainViewIntent = new Intent(getApplicationContext(), mainViewActivity.class);
                startActivity(mainViewIntent);
            }
        });

    }
}
