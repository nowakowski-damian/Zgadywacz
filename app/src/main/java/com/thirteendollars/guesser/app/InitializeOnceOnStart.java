package com.thirteendollars.guesser.app;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.thirteendollars.guesser.other.MediaManager;
import com.thirteendollars.guesser.wordslibrary.DateBaseCopyingManager;

import java.io.IOException;

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


        //COPY ANDROID_WORDS SQLITE DATABASE IF OPENED FIRST
        boolean firstAppOpening=AppStaticData.sp.getBoolean("FIRST_APP_OPENING",true);
        if(firstAppOpening){

            DateBaseCopyingManager copyingManager =new DateBaseCopyingManager(getApplicationContext());
            if( !copyingManager.isDBexist() )
                try { copyingManager.copyDataBase(); }
                    catch(IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

            AppStaticData.spEditor.putBoolean("FIRST_APP_OPENING",false);
        }

    }








}
