package com.thirteendollars.guesser.app;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.data.LevelData;


public class MainMenu extends AppCompatActivity {


    private final String appid = AppStaticData.playAppID;
    public static MediaPlayer player;
    private ImageView musicIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        AppStaticData.pUser = ParseUser.getCurrentUser();
        AppStaticData.userName = ParseUser.getCurrentUser().getUsername();

        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
        }
        musicIcon=(ImageView)findViewById(R.id.actionbar_music_icon);
        ((TextView)findViewById(R.id.mainmenu_actionbar_username_text)).setText(AppStaticData.userName);
    }

    @Override
    protected void onResume() {
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
        super.onResume();
        if (AppStaticData.sp.getBoolean("rankingNotCreate", true)) createRankingPlace();
        else updateRankingPlace();

        if(AppStaticData.PLAY_MUSIC && (player==null || !player.isPlaying()) ) {
            player = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            player.setLooping(true);
            player.start();
        }
        setProperMusicIcon();

    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }


    @Override
    public void onBackPressed() {
        if(AppStaticData.PLAY_MUSIC && player!=null) player.stop();
            super.onBackPressed();
    }

    private void createRankingPlace() {
        AppStaticData.spEditor.putBoolean("rankingNotCreate", false);
        AppStaticData.spEditor.commit();
        ParseObject ranking = new ParseObject("Ranking");
        ranking.put("playerId", AppStaticData.pUser.getObjectId());
        ranking.put("playerName", AppStaticData.pUser.getString("username"));
        ranking.put("levelPercentage", 0);
        ranking.put("ownWordsNum", 0);
        ranking.saveInBackground();
    }

    public static void updateRankingPlace() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ranking");
        query.whereEqualTo("playerId", AppStaticData.pUser.getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject ranking, ParseException e) {
                if (e == null) {
                    ranking.put("levelPercentage", new LevelData().getLevelPercentage());
                    ranking.put("ownWordsNum", AppStaticData.sp.getInt("savedNumberOfOwnWords", 0));
                    ranking.saveInBackground();
                }
            }
        });
    }




    public void onPlay(View view) {
        startActivity(new Intent(this, PlayActivity.class));
    }

    public void onRanking(View view) {
        startActivity(new Intent(this, Ranking.class));
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

    public void onLogout(View view) {
        ParseUser.logOutInBackground();
        AppStaticData.resetUserData();
        if( player!=null ) {
            player.stop();
            player = null;
        }
        this.finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }


    public void changeMusicSettings(View view) {
        AppStaticData.changeMusicSettings();
        if(player!=null && player.isPlaying()) player.pause();
        else if(player!=null) player.start();
        else if(player==null && AppStaticData.PLAY_MUSIC){
            player = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
            player.setLooping(true);
            player.start();
        }
        setProperMusicIcon();

    }

    private void setProperMusicIcon(){
        if(AppStaticData.PLAY_MUSIC) musicIcon.setImageResource(R.drawable.volume_on);
        else  musicIcon.setImageResource(R.drawable.volume_off);
    }

}





