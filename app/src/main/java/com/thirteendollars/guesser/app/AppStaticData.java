package com.thirteendollars.guesser.app;

import android.content.SharedPreferences;
import android.view.View;

import com.thirteendollars.guesser.data.ERROR;

/**
 * Created by Damian on 2015-12-12.
 */
public class AppStaticData {



    public final static String playAppID="com.thirteendollars.zgadywacz";



    //CONSTANCE
    public static final int MIN_WORD_LENGTH = 3;
    public static final int MAX_WORD_LENGHT_ADNROID = 20;
    public static final int MAX_GAME_LEVEL=25;
    public static final int MAX_TIME_LEVEL=25;
    public static final int MAX_TRIES_LEVEL=25;
    public static final int MAX_START_LETTERS_LEVEL=5;

    public static final int FLAGS= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;




    //SHARED PREFERENCES
    public static SharedPreferences sp;
    public static SharedPreferences.Editor spEditor;

    //SCREEN FEATURES
    public static int screenWidth;
    public static int screenHeight;
    public static int screenDensity;

    //GAME SETTINGS
    public static int WORDS_LENGTH;
    public static boolean PLAY_MUSIC;


    // GAME SETTINGS FUNCTIONS

    static public void loadSettingsFromSP(){
        WORDS_LENGTH=sp.getInt("WORDS_LENGTH",3);
    }


    static public int saveSettingsToSP(){
        spEditor.putInt("WORDS_LENGTH", WORDS_LENGTH);
        if( spEditor.commit() ) return ERROR.NO_ERRORS;
        else return ERROR.COMMIT_ERROR_AT_AppStaticData;
    }


    static public boolean loadMusicSettings(){
        PLAY_MUSIC=sp.getBoolean("musicSettings",true);
        return PLAY_MUSIC;
    }

    static public void changeMusicSettings(){
        PLAY_MUSIC=!PLAY_MUSIC;
        spEditor.putBoolean("musicSettings",PLAY_MUSIC);
        spEditor.commit();
    }


}

