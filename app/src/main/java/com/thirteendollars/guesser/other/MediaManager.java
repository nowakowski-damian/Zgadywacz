package com.thirteendollars.guesser.other;

import android.content.Context;
import android.media.MediaPlayer;

import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.app.AppStaticData;
import com.thirteendollars.guesser.app.MainMenu;

/**
 * Created by Damian on 2016-01-28.
 */
public class MediaManager {



    private static MediaPlayer[] tournamentMusic;
    private static MediaPlayer buttonLimit;
    private static MediaPlayer endgameCash;
    private static MediaPlayer upgraded;
    private static int musicNumber;
    private static boolean initialized=false;



    public static void initialize(Context context){
        tournamentMusic=new MediaPlayer[9];
        tournamentMusic[0]=MediaPlayer.create(context, R.raw.b1);
        tournamentMusic[1]=MediaPlayer.create(context, R.raw.b2);
        tournamentMusic[2]=MediaPlayer.create(context, R.raw.b3);
        tournamentMusic[3]=MediaPlayer.create(context, R.raw.b4);
        tournamentMusic[4]=MediaPlayer.create(context, R.raw.b5);
        tournamentMusic[5]=MediaPlayer.create(context, R.raw.b6);
        tournamentMusic[6]=MediaPlayer.create(context, R.raw.b7);
        tournamentMusic[7]=MediaPlayer.create(context, R.raw.b8);
        tournamentMusic[8]=MediaPlayer.create(context, R.raw.b9);
        for(int i=0;i<9;i++) tournamentMusic[i].setLooping(true);
        buttonLimit=MediaPlayer.create(context, R.raw.button_limit);
        endgameCash=MediaPlayer.create(context, R.raw.endgame_cash);
        upgraded=MediaPlayer.create(context, R.raw.drdre);
        musicNumber=AppStaticData.sp.getInt("musicBackgroundNumber",0);
        initialized=true;
    }

    public static boolean isInitialized(){
        return initialized;
    }


    public static void playTournamentMusic() {
        if (AppStaticData.PLAY_MUSIC) tournamentMusic[musicNumber].start();
    }


    public static void stopTournamentMusic(){

        if (AppStaticData.PLAY_MUSIC) {

            tournamentMusic[musicNumber].pause();
            tournamentMusic[musicNumber].seekTo(0);
            //select next soundtrack
            if (musicNumber >= 8) musicNumber = 0;
            else musicNumber++;
            AppStaticData.spEditor.putInt("musicBackgroundNumber", musicNumber);
            AppStaticData.spEditor.commit();

        }
    }

    public static void playButtonNo(){
        if (AppStaticData.PLAY_MUSIC) {

            if (buttonLimit.isPlaying()) buttonLimit.seekTo(0);
            else buttonLimit.start();

        }
    }

    public static void playEndgame(){
        if (AppStaticData.PLAY_MUSIC) {

            if (buttonLimit.isPlaying()) buttonLimit.seekTo(0);
            else endgameCash.start();

        }
    }

    public static void playUpgrade(){
        if (AppStaticData.PLAY_MUSIC) {
            upgraded.seekTo(0);
            upgraded.start();
            upgraded.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                }
            });

        }
    }




}
