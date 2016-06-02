package com.thirteendollars.guesser.other;

import android.view.View;

import com.thirteendollars.guesser.R;

/**
 * Created by Damian on 2016-01-08.
 */
public class MyKeyboard {

    private char letter;
    private String keybord="QWEĘRTYUIOÓPAĄSŚDFGHJKLŁZŹŻXCĆVBNŃM";
    public static char DELETE='<';


    public char getLetterOnClick(View view){

        switch (view.getId()){
            case R.id.K1: letter=keybord.charAt(0); break;
            case R.id.K2: letter=keybord.charAt(1); break;
            case R.id.K3: letter=keybord.charAt(2); break;
            case R.id.K4: letter=keybord.charAt(3); break;
            case R.id.K5: letter=keybord.charAt(4); break;
            case R.id.K6: letter=keybord.charAt(5); break;
            case R.id.K7: letter=keybord.charAt(6); break;
            case R.id.K8: letter=keybord.charAt(7); break;
            case R.id.K9: letter=keybord.charAt(8); break;
            case R.id.K10: letter=keybord.charAt(9); break;
            case R.id.K11: letter=keybord.charAt(10); break;
            case R.id.K12: letter=keybord.charAt(11); break;
            case R.id.K13: letter=keybord.charAt(12); break;
            case R.id.K14: letter=keybord.charAt(13); break;
            case R.id.K15: letter=keybord.charAt(14); break;
            case R.id.K16: letter=keybord.charAt(15); break;
            case R.id.K17: letter=keybord.charAt(16); break;
            case R.id.K18: letter=keybord.charAt(17); break;
            case R.id.K19: letter=keybord.charAt(18); break;
            case R.id.K20: letter=keybord.charAt(19); break;
            case R.id.K21: letter=keybord.charAt(20); break;
            case R.id.K22: letter=keybord.charAt(21); break;
            case R.id.K23: letter=keybord.charAt(22); break;
            case R.id.K24: letter=keybord.charAt(23); break;
            case R.id.K25: letter=keybord.charAt(24); break;
            case R.id.K26: letter=keybord.charAt(25); break;
            case R.id.K27: letter=keybord.charAt(26); break;
            case R.id.K28: letter=keybord.charAt(27); break;
            case R.id.K29: letter=keybord.charAt(28); break;
            case R.id.K30: letter=keybord.charAt(29); break;
            case R.id.K31: letter=keybord.charAt(30); break;
            case R.id.K32: letter=keybord.charAt(31); break;
            case R.id.K33: letter=keybord.charAt(32); break;
            case R.id.K34: letter=keybord.charAt(33); break;
            case R.id.K35: letter=keybord.charAt(34); break;
            case R.id.K36: letter= DELETE; break;
            default: letter='*';
        }

        return letter;
    }

}
