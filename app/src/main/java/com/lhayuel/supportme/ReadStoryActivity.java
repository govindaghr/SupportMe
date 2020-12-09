package com.lhayuel.supportme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ReadStoryActivity extends AppCompatActivity {
    ImageView postImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        postImage = findViewById(R.id.postImage);
    }
}