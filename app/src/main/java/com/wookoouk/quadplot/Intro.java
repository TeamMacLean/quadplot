package com.wookoouk.quadplot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Intro extends AppIntro {
//public class Intro extends AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int color = ContextCompat.getColor(this, R.color.intro_flat_orange);
        addSlide(AppIntroFragment.newInstance("Welcome to QuadPlot", "by Team MacLean.", R.drawable.logo, color));

        // Wait for ~3m
        color = ContextCompat.getColor(this, R.color.intro_flat_yellow);
        addSlide(AppIntroFragment.newInstance("GPS", "Wait for the GPS signal to be as accurate as it can be (68% to 3m).", R.drawable.intro_gps, color));

        // Ad plot at each location
        color = ContextCompat.getColor(this, R.color.intro_flat_blue);
        addSlide(AppIntroFragment.newInstance("Add a plot", "Go up to a plot, press the + and select the plots height.", R.drawable.intro_pin, color));

        // Connect to quad
        color = ContextCompat.getColor(this, R.color.intro_flat_mint);
        addSlide(AppIntroFragment.newInstance("Connect", "Connect to your drone.", R.drawable.tx, color));

        // Upload
        color = ContextCompat.getColor(this, R.color.intro_flat_green);
        addSlide(AppIntroFragment.newInstance("Upload", "Press 'Upload' and wait for confirmation.", R.drawable.logo, color));

        // Start auto mode
        color = ContextCompat.getColor(this, R.color.intro_flat_purple);
        addSlide(AppIntroFragment.newInstance("Lift Off", "Grab your drone's transmitter and switch it to Auto mode.", R.drawable.logo, color));

        setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        returnToApp();


    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        returnToApp();
    }

    private void returnToApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}