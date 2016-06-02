package com.thirteendollars.guesser.data;

import android.content.SharedPreferences;

import com.thirteendollars.guesser.app.AppStaticData;

/**
 * Created by Damian on 2016-01-01.
 */
public class LevelData {


    private final String ID="LEVEL01";
    private int level;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public LevelData() {
        sp= AppStaticData.sp;
        spEditor=AppStaticData.spEditor;
        this.level =sp.getInt(ID,1);
    }

    public int increase(){
       if(level<AppStaticData.MAX_GAME_LEVEL) {
           level++;
           spEditor.putInt(ID, level);
           if( spEditor.commit() ) return ERROR.NO_ERRORS;
           else{
               level--;
               return ERROR.COMMIT_ERROR_AT_LevelData;
           }
       }
       else return ERROR.ACHIEVED_MAX_LEVEL;
    }

    public int getLevel(){
        return level;
    }




    public int getCost(int forLevel){
        int cost;

        switch(forLevel){
            case 2: cost=100; break;
            case 3: cost=300; break;
            case 4: cost=500; break;
            case 5: cost=1000; break;
            case 6: cost=1500; break;
            case 7: cost=2000; break;
            case 8: cost=2500; break;
            case 9: cost=3250; break;
            case 10: cost=4000; break;
            case 11: cost=4750; break;
            case 12: cost=5500; break;
            case 13: cost=6250; break;
            case 14: cost=7250; break;
            case 15: cost=8250; break;
            case 16: cost=9250; break;
            case 17: cost=10250; break;
            case 18: cost=12500; break;
            case 19: cost=15000; break;
            case 20: cost=20000; break;
            case 21: cost=25000; break;
            case 22: cost=50000; break;
            case 23: cost=75000; break;
            case 24: cost=100000; break;
            case 25: cost=150000; break;
            default: cost=ERROR.INCORRECT_ARGUMENT_AT_LevelData;
        }
        return cost;
    }



    public int getMaxWordLength(int forLevel){
        int maxWordLength;

        switch(forLevel){
            case 1: maxWordLength=3; break;
            case 2: maxWordLength=4; break;
            case 3: maxWordLength=5; break;
            case 4: maxWordLength=6; break;
            case 5: maxWordLength=7; break;
            case 6: maxWordLength=8; break;
            case 7: maxWordLength=9; break;
            case 8: maxWordLength=10; break;
            case 9: maxWordLength=11; break;
            case 10: maxWordLength=12; break;
            case 11: maxWordLength=13; break;
            case 12: maxWordLength=14; break;
            case 13: maxWordLength=15; break;
            case 14: maxWordLength=16; break;
            case 15: maxWordLength=17; break;
            case 16: maxWordLength=18; break;
            case 17: maxWordLength=19; break;
            case 18: maxWordLength=20; break;
            case 19: maxWordLength=21; break;
            case 20: maxWordLength=22; break;
            case 21: maxWordLength=23; break;
            case 22: maxWordLength=24; break;
            case 23: maxWordLength=25; break;
            case 24: maxWordLength=25; break;
            case 25: maxWordLength=25; break;
            default: maxWordLength=ERROR.INCORRECT_ARGUMENT_AT_LevelData;
        }
        return maxWordLength;

    }

    public int getMaxWordLengthForCurrentLevel(){
        return getMaxWordLength(level);
    }

    public int getMaxOwnWords(int forLevel){
        int maxOwnWords;

        switch(forLevel){
            case 1: maxOwnWords=0; break;
            case 2: maxOwnWords=0; break;
            case 3: maxOwnWords=0; break;
            case 4: maxOwnWords=1; break;
            case 5: maxOwnWords=1; break;
            case 6: maxOwnWords=1; break;
            case 7: maxOwnWords=1; break;
            case 8: maxOwnWords=2; break;
            case 9: maxOwnWords=2; break;
            case 10: maxOwnWords=2; break;
            case 11: maxOwnWords=2; break;
            case 12: maxOwnWords=2; break;
            case 13: maxOwnWords=3; break;
            case 14: maxOwnWords=3; break;
            case 15: maxOwnWords=3; break;
            case 16: maxOwnWords=3; break;
            case 17: maxOwnWords=3; break;
            case 18: maxOwnWords=4; break;
            case 19: maxOwnWords=4; break;
            case 20: maxOwnWords=5; break;
            case 21: maxOwnWords=5; break;
            case 22: maxOwnWords=6; break;
            case 23: maxOwnWords=8; break;
            case 24: maxOwnWords=10; break;
            case 25: maxOwnWords=10; break;
            default: maxOwnWords=ERROR.INCORRECT_ARGUMENT_AT_LevelData;
        }
        return maxOwnWords;

    }

    public int getLevelPercentage(){
        return level*4;
    }


    public int getMaxOwnWordsForCurrentLevel(){
        return getMaxOwnWords(level);
    }


}
