package com.codepath.photospot.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.photospot.R;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // Done in the Application class...
        //Parse.initialize(this);

    }
}
