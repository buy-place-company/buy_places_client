package ru.tp.buy_places.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import ru.tp.buy_places.R;

public class SplashScreenActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ImageView splashImageView =
                (ImageView) findViewById(R.id.SplashImageView);
        splashImageView.setBackgroundResource(R.drawable.splash);
        final AnimationDrawable frameAnimation =
                (AnimationDrawable)splashImageView.getBackground();
        splashImageView.post(new Runnable(){
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
        final int SPLASH_SCREEN_DURATION = getResources().getInteger(R.integer.splash_screen_duration);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.start(SplashScreenActivity.this);
                finish();
            }
        }, SPLASH_SCREEN_DURATION);
    }



    @Override
    public void onBackPressed() {

    }
}
