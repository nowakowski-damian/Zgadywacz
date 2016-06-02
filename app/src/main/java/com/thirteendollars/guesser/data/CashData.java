package com.thirteendollars.guesser.data;

import android.content.SharedPreferences;

import com.thirteendollars.guesser.app.AppStaticData;

/**
 * Created by Damian on 2016-01-01.
 */

public class CashData {




    private final String ID="CASH01";
    private int cash;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public CashData() {
        sp= AppStaticData.sp;
        spEditor=AppStaticData.spEditor;
        this.cash =sp.getInt(ID,0);
    }

    public int changeBalance(int value){
        cash+=value;
        spEditor.putInt(ID, cash);
        if( spEditor.commit() ) return ERROR.NO_ERRORS;
        else return ERROR.COMMIT_ERROR_AT_CashData;
    }



    public int getBalance(){
        return cash;
    }


    public static int countChange(int correct,int incorrect,int triesLeft,int WORDS_MODE) {

        int length = incorrect + correct;
        boolean isWin = (incorrect == 0);

        int fromCorrects = countCashFromCorrectLetters(correct, WORDS_MODE);
        int fromIncorrects = countCashFromIncorrectLetters(incorrect, length, WORDS_MODE);
        int sum = fromCorrects + fromIncorrects;

        if (isWin) sum += countCashFromWinBonus(length, WORDS_MODE) + countCashFromTriesBonus(length, triesLeft);

        return sum;
    }


    public static int countCashFromCorrectLetters(int num,int WORDS_MODE){

        switch(WORDS_MODE){
            case AppStaticData.FROM_ANDROID: return num;
            case AppStaticData.FROM_USERS: return num*10;
            default: return 0;
        }
    }

    public static int countCashFromIncorrectLetters(int num,int length,int WORDS_MODE){

        switch(WORDS_MODE){
            case AppStaticData.FROM_ANDROID: return 0;
            case AppStaticData.FROM_USERS: return -(num*length);
            default: return 0;
        }
    }

    public static int countCashFromWinBonus(int length,int WORDS_MODE){

        switch(WORDS_MODE){
            case AppStaticData.FROM_ANDROID: return 5*length;
            case AppStaticData.FROM_USERS: return 5*length*length;
            default: return 0;
        }
    }

    public static int countCashFromTriesBonus(int length,int triesLeft){
        return (length-1)*(triesLeft-1);
    }

    public static int countCashFromIncomes(int incomes){
        return incomes*250;
    }


}
