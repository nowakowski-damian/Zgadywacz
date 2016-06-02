package com.thirteendollars.guesser.data;

import android.content.SharedPreferences;

import com.thirteendollars.guesser.app.AppStaticData;

/**
 * Created by Damian on 2016-01-01.
 */
public class TimeData {


    private final String ID="TIME01";
    private int timeLevel;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public TimeData() {
        sp= AppStaticData.sp;
        spEditor=AppStaticData.spEditor;
        this.timeLevel =sp.getInt(ID,1);
    }

    public int increase(){
        if(timeLevel<AppStaticData.MAX_TIME_LEVEL) {
            timeLevel++;
            spEditor.putInt(ID, timeLevel);
            if( spEditor.commit() ) return ERROR.NO_ERRORS;
            else{
                timeLevel--;
                return ERROR.COMMIT_ERROR_AT_TimeData;
            }
        }
        else return ERROR.ACHIEVED_MAX_LEVEL;
    }

    public int getTimeLevel(){
        return timeLevel;
    }




    public int getCost(int forLevel){
        int cost;

        switch(forLevel){
            case 2: cost=100; break;
            case 3: cost=225; break;
            case 4: cost=400; break;
            case 5: cost=625; break;
            case 6: cost=900; break;
            case 7: cost=1225; break;
            case 8: cost=1600; break;
            case 9: cost=2025; break;
            case 10: cost=2500; break;
            case 11: cost=3025; break;
            case 12: cost=3600; break;
            case 13: cost=4225; break;
            case 14: cost=4900; break;
            case 15: cost=5625; break;
            case 16: cost=6400; break;
            case 17: cost=7225; break;
            case 18: cost=8100; break;
            case 19: cost=9025; break;
            case 20: cost=10000; break;
            case 21: cost=11025; break;
            case 22: cost=12100; break;
            case 23: cost=13225; break;
            case 24: cost=14400; break;
            case 25: cost=15625; break;
            default: cost=ERROR.INCORRECT_ARGUMENT_AT_TimeData;
        }
        return cost;
    }


    public int getMinLevel(int forLevel){
        int minLevel;

        switch(forLevel){
            case 2: minLevel=1; break;
            case 3: minLevel=1; break;
            case 4: minLevel=1; break;
            case 5: minLevel=1; break;
            case 6: minLevel=2; break;
            case 7: minLevel=2; break;
            case 8: minLevel=2; break;
            case 9: minLevel=3; break;
            case 10: minLevel=4; break;
            case 11: minLevel=5; break;
            case 12: minLevel=6; break;
            case 13: minLevel=7; break;
            case 14: minLevel=8; break;
            case 15: minLevel=9; break;
            case 16: minLevel=10; break;
            case 17: minLevel=11; break;
            case 18: minLevel=12; break;
            case 19: minLevel=13; break;
            case 20: minLevel=14; break;
            case 21: minLevel=15; break;
            case 22: minLevel=16; break;
            case 23: minLevel=17; break;
            case 24: minLevel=18; break;
            case 25: minLevel=20; break;
            default: minLevel=ERROR.INCORRECT_ARGUMENT_AT_TimeData;
        }
        return minLevel;

    }




    public int getTimePerLetter(int forLevel){
        int timePerLetter;

        switch(forLevel){
            case 1: timePerLetter=10; break;
            case 2: timePerLetter=11; break;
            case 3: timePerLetter=12; break;
            case 4: timePerLetter=13; break;
            case 5: timePerLetter=14; break;
            case 6: timePerLetter=16; break;
            case 7: timePerLetter=18; break;
            case 8: timePerLetter=20; break;
            case 9: timePerLetter=23; break;
            case 10: timePerLetter=27; break;
            case 11: timePerLetter=32; break;
            case 12: timePerLetter=37; break;
            case 13: timePerLetter=42; break;
            case 14: timePerLetter=47; break;
            case 15: timePerLetter=52; break;
            case 16: timePerLetter=57; break;
            case 17: timePerLetter=62; break;
            case 18: timePerLetter=67; break;
            case 19: timePerLetter=72; break;
            case 20: timePerLetter=77; break;
            case 21: timePerLetter=82; break;
            case 22: timePerLetter=87; break;
            case 23: timePerLetter=92; break;
            case 24: timePerLetter=97; break;
            case 25: timePerLetter=120; break;
            default: timePerLetter=ERROR.INCORRECT_ARGUMENT_AT_TimeData;
        }
        return timePerLetter;

    }

    public int getTimePerLetterForActualLevel(){
        return getTimePerLetter(timeLevel);
    }



}


