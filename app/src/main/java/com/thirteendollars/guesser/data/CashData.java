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


    public static int countChange(int correct,int incorrect,int triesLeft) {

        int length = incorrect + correct;
        boolean isWin = (incorrect == 0);

        int fromCorrects = countCashFromCorrectLetters(correct);
        int fromIncorrects = countCashFromIncorrectLetters(incorrect, length);
        int sum = fromCorrects + fromIncorrects;

        if (isWin) sum += countCashFromWinBonus(length) + countCashFromTriesBonus(length, triesLeft);

        return sum;
    }


    public static int countCashFromCorrectLetters(int num){
        return num;
    }

    public static int countCashFromIncorrectLetters(int num,int length){
        return -(num*length);
    }

    public static int countCashFromWinBonus(int length){
        return 5*length;
    }

    public static int countCashFromTriesBonus(int length,int triesLeft){
        return (length-1)*(triesLeft-1);
    }

    public static int countCashFromIncomes(int incomes){
        return incomes*250;
    }


}
