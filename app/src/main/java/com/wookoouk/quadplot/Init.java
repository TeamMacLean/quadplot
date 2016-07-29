package com.wookoouk.quadplot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Init extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Util.hasSeenIntro(this)) {
            startDefaultIntro();
            Util.hasSeenIntro(this, true);
        } else {
            startMain();
        }
    }

    private void startDefaultIntro() {
        Intent intent = new Intent(this, Intro.class);
        startActivity(intent);
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
