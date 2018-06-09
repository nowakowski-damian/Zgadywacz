package com.thirteendollars.guesser.app;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.thirteendollars.guesser.R;


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





