package com.thirteendollars.guesser.app;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.thirteendollars.guesser.other.MediaManager;

/**
 * Created by Damian on 2015-12-11.
 */
public class InitializeOnceOnStart extends Application {


    @Override
    public void onCreate() {

        //SET UP SHARED PREFERENCES
        AppStaticData.sp=getSharedPreferences("SharedData", MODE_PRIVATE);
        AppStaticData.spEditor=AppStaticData.sp.edit();
        AppStaticData.loadMusicSettings();
        super.onCreate();

        //INITILIZE MUSIC
        if( !MediaManager.isInitialized() ) MediaManager.initialize(getApplicationContext());

        // LOAD SCREEN FEATURES
        WindowManager windowManager=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        display.getMetrics(metrics);
        AppStaticData.screenHeight=metrics.heightPixels;
        AppStaticData.screenWidth=metrics.widthPixels;
        AppStaticData.screenDensity=metrics.densityDpi;


        //LOAD GAME SETTINGS
        AppStaticData.loadSettingsFromSP();
    }
}
