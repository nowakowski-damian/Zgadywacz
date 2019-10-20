package com.thirteendollars.guesser.app;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.wordslibrary.DatabaseManager;


public class MainMenu extends AppCompatActivity {


    private final String appid = AppStaticData.playAppID;
    private ImageView musicIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
        }
        musicIcon=(ImageView)findViewById(R.id.actionbar_music_icon);
        initializeDatabase();
    }

    private void initializeDatabase() {
        final LinearLayout buttons = findViewById(R.id.linearLayout);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        DatabaseManager.getInstance(this).initialize(new DatabaseManager.DatabaseCallback() {
            @Override
            public void onDatabaseInitializationStarted() {
                Toast.makeText(getApplicationContext(), R.string.db_initialization_started, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDatabaseAlreadyExists() {
                buttons.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onDatabasePopulatedSuccess() {
                buttons.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onDatabasePopulatedError() {
                Toast.makeText(getApplicationContext(), R.string.db_initialization_error, Toast.LENGTH_LONG).show();
                buttons.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
        super.onResume();

        setProperMusicIcon();

    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }

    public void onPlay(View view) {
        startActivity(new Intent(this, PlayActivity.class));
    }

    public void onCredits(View view) {
        startActivity(new Intent(this, Credits.class));
    }

    public void onRateMe(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + appid));
        if (!openGooglePlay(intent)) {
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?=" + appid));
            if (!openGooglePlay(intent)) {
                Toast.makeText(getApplicationContext(), R.string.on_rate_me_googleplay_error, Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private boolean openGooglePlay(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }


    public void changeMusicSettings(View view) {
        AppStaticData.changeMusicSettings();
        setProperMusicIcon();

    }

    private void setProperMusicIcon(){
        if(AppStaticData.PLAY_MUSIC) musicIcon.setImageResource(R.drawable.volume_on);
        else  musicIcon.setImageResource(R.drawable.volume_off);
    }

}





