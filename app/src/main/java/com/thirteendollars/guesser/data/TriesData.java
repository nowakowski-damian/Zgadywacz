package com.thirteendollars.guesser.data;

import android.content.SharedPreferences;

import com.thirteendollars.guesser.app.AppStaticData;

/**
 * Created by Damian on 2016-01-01.
 */
public class TriesData {




    private final String ID="TRIES01";
    private int triesLevel;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public TriesData() {
        sp= AppStaticData.sp;
        spEditor=AppStaticData.spEditor;
        this.triesLevel =sp.getInt(ID,1);
    }


    public int increase(){
        if(triesLevel<AppStaticData.MAX_TRIES_LEVEL) {
            triesLevel++;
            spEditor.putInt(ID, triesLevel);
            if( spEditor.commit() ) return ERROR.NO_ERRORS;
            else{
                triesLevel--;
                return ERROR.COMMIT_ERROR_AT_TriesData;
            }
        }
        else return ERROR.ACHIEVED_MAX_LEVEL;
    }


    public int getTriesLevel(){
        return triesLevel;
    }




    public int getCost(int forLevel){
        int cost;

        switch(forLevel){
            case 2: cost=100; break;
            case 3: cost=300; break;
            case 4: cost=500; break;
            case 5: cost=800; break;
            case 6: cost=1100; break;
            case 7: cost=1500; break;
            case 8: cost=2000; break;
            case 9: cost=2600; break;
            case 10: cost=3300; break;
            case 11: cost=4100; break;
            case 12: cost=5000; break;
            case 13: cost=6000; break;
            case 14: cost=7100; break;
            case 15: cost=8300; break;
            case 16: cost=9600; break;
            case 17: cost=11000; break;
            case 18: cost=12500; break;
            case 19: cost=14100; break;
            case 20: cost=15800; break;
            case 21: cost=17600; break;
            case 22: cost=19500; break;
            case 23: cost=21500; break;
            case 24: cost=23500; break;
            case 25: cost=30000; break;
            default: cost=ERROR.INCORRECT_ARGUMENT_AT_TriesData;
        }
    return cost;
    }


    public int getMinLevel(int forLevel){
        int minLevel;

        switch(forLevel){
            case 2: minLevel=1; break;
            case 3: minLevel=2; break;
            case 4: minLevel=2; break;
            case 5: minLevel=3; break;
            case 6: minLevel=3; break;
            case 7: minLevel=4; break;
            case 8: minLevel=5; break;
            case 9: minLevel=6; break;
            case 10: minLevel=7; break;
            case 11: minLevel=8; break;
            case 12: minLevel=9; break;
            case 13: minLevel=10; break;
            case 14: minLevel=11; break;
            case 15: minLevel=12; break;
            case 16: minLevel=13; break;
            case 17: minLevel=14; break;
            case 18: minLevel=15; break;
            case 19: minLevel=16; break;
            case 20: minLevel=17; break;
            case 21: minLevel=18; break;
            case 22: minLevel=19; break;
            case 23: minLevel=20; break;
            case 24: minLevel=20; break;
            case 25: minLevel=20; break;
            default: minLevel=ERROR.INCORRECT_ARGUMENT_AT_TriesData;
        }
        return minLevel;

    }

    public int getTriesPerLetter(int forLevel){
        int triesPerLetter;

        switch(forLevel){
            case 1: triesPerLetter=2; break;
            case 2: triesPerLetter=3; break;
            case 3: triesPerLetter=4; break;
            case 4: triesPerLetter=5; break;
            case 5: triesPerLetter=6; break;
            case 6: triesPerLetter=7; break;
            case 7: triesPerLetter=8; break;
            case 8: triesPerLetter=9; break;
            case 9: triesPerLetter=10; break;
            case 10: triesPerLetter=11; break;
            case 11: triesPerLetter=12; break;
            case 12: triesPerLetter=13; break;
            case 13: triesPerLetter=14; break;
            case 14: triesPerLetter=15; break;
            case 15: triesPerLetter=16; break;
            case 16: triesPerLetter=17; break;
            case 17: triesPerLetter=18; break;
            case 18: triesPerLetter=19; break;
            case 19: triesPerLetter=20; break;
            case 20: triesPerLetter=21; break;
            case 21: triesPerLetter=22; break;
            case 22: triesPerLetter=23; break;
            case 23: triesPerLetter=24; break;
            case 24: triesPerLetter=25; break;
            case 25: triesPerLetter=30; break;
            default: triesPerLetter=ERROR.INCORRECT_ARGUMENT_AT_TriesData;
        }
        return triesPerLetter;

    }

    public int getTriesPerLetterForActualLevel(){
        return getTriesPerLetter(triesLevel);
    }



}
