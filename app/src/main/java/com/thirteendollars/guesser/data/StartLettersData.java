package com.thirteendollars.guesser.data;

import android.content.SharedPreferences;

import com.thirteendollars.guesser.app.AppStaticData;

/**
 * Created by Damian on 2016-01-01.
 */

public class StartLettersData {





    private final String ID="STARTLETTERS01";
    private int startLettersLevel;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public StartLettersData() {
        sp= AppStaticData.sp;
        spEditor=AppStaticData.spEditor;
        this.startLettersLevel =sp.getInt(ID,1);
    }

    public int increase(){
        if(startLettersLevel<AppStaticData.MAX_START_LETTERS_LEVEL) {
            startLettersLevel++;
            spEditor.putInt(ID, startLettersLevel);
            if( spEditor.commit() ) return ERROR.NO_ERRORS;
            else{
                startLettersLevel--;
                return ERROR.COMMIT_ERROR_AT_StartLettersData;
            }
        }
        else return ERROR.ACHIEVED_MAX_LEVEL;
    }

    public int getStartLettersLevel(){
        return startLettersLevel;
    }



    public int getCost(int forLevel){

        int cost;

        switch(forLevel){
            case 2: cost=5000; break;
            case 3: cost=15000; break;
            case 4: cost=30000; break;
            case 5: cost=75000; break;
            default: cost=ERROR.INCORRECT_ARGUMENT_AT_StartLettersData;
        }
        return cost;
    }


    public int getMinLevel(int forLevel){

        int minLevel;

        switch(forLevel){
            case 2: minLevel=5; break;
            case 3: minLevel=15; break;
            case 4: minLevel=20; break;
            case 5: minLevel=24; break;
            default: minLevel=ERROR.INCORRECT_ARGUMENT_AT_StartLettersData;
        }
        return minLevel;
    }


    public int getLettersOnStart(int forLevel){

        int lettersOnStart;

        switch(forLevel){
            case 1: lettersOnStart=1; break;
            case 2: lettersOnStart=2; break;
            case 3: lettersOnStart=3; break;
            case 4: lettersOnStart=4; break;
            case 5: lettersOnStart=5; break;
            default: lettersOnStart=ERROR.INCORRECT_ARGUMENT_AT_StartLettersData;
        }
        return lettersOnStart;


    }


    public int getLettersOnStartForActualLevel(){
        return getLettersOnStart(startLettersLevel);
    }


}
