package com.thirteendollars.guesser.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.data.LevelData;
import com.thirteendollars.guesser.other.MediaManager;
import com.thirteendollars.guesser.other.MyKeyboard;
import com.thirteendollars.guesser.wordslibrary.DatabaseManager;

public class AddWordActivity extends AppCompatActivity {



    private MyKeyboard keyboard;
    private int maxWordLength;
    private int margins;
    private int letterSize;
    private int wordLength;
    private String mainWord="";
    private LinearLayout spaceForLetters;
    ViewSwitcher switcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
        wordLength=0;
        keyboard=new MyKeyboard();
        maxWordLength=new LevelData().getMaxWordLengthForCurrentLevel();
        spaceForLetters=(LinearLayout)findViewById(R.id.add_word_letter_freespace);
        switcher=(ViewSwitcher)findViewById(R.id.add_word_view_switcher);
        setUpLetterMarginsAndSize();

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }




    private void setUpLetterMarginsAndSize() {
        margins= AppStaticData.screenDensity/20;
        if( maxWordLength>20)margins=2;
        else if ( maxWordLength>15) margins/=2;
        else if( maxWordLength>10) margins*=2./3;
        letterSize= AppStaticData.screenHeight /   maxWordLength;
        letterSize-=margins*2;
        int maxSize=AppStaticData.screenWidth/6;
        if(letterSize>maxSize) letterSize=maxSize;
    }


    public void onCancel(View view) {
        finish();
    }

    public void onCommit(View view) {
        if ( AppStaticData.sp.getInt("savedNumberOfOwnWords",0)>= new LevelData().getMaxOwnWordsForCurrentLevel() ) { Toast.makeText(getApplicationContext(), R.string.add_word_achieved_limit ,Toast.LENGTH_SHORT).show(); finish(); return;}
        switcher.showNext();
        if( mainWord.length() <3 ){
            Toast.makeText(getApplicationContext(), R.string.add_word_too_short,Toast.LENGTH_SHORT).show();
            switcher.showPrevious();
            return;
        }
        increaseOwnWordsCounter();
        if ( isWordInDB() ) sendToUserWords();
        else sendToToConfirm();
        finish();
    }

    private void increaseOwnWordsCounter() {
        int num=AppStaticData.sp.getInt("savedNumberOfOwnWords", 0);
        num++;
        AppStaticData.spEditor.putInt("savedNumberOfOwnWords",num);
        AppStaticData.spEditor.commit();
    }


    private void sendToToConfirm() {

        ParseObject word=new ParseObject("ToConfirm");
        word.put( "ownerId",AppStaticData.pUser.getObjectId() );
        word.put("length",mainWord.length() );
        word.put("word", mainWord);
        try {
            word.save();
        } catch (ParseException e) {
            e.printStackTrace();
            word.saveInBackground();
        }

        Toast.makeText(getApplicationContext(), R.string.add_word_sended_to_confirm,Toast.LENGTH_LONG).show();
    }

    private void sendToUserWords() {

        ParseObject word=new ParseObject("UserWords");
        word.put( "ownerId", AppStaticData.pUser.getObjectId() );
        word.put("length",mainWord.length() );
        word.put("word",mainWord );
        word.put("casualties", 0);
        word.saveInBackground();
        Toast.makeText(getApplicationContext(), R.string.add_word_addition_success,Toast.LENGTH_SHORT).show();
    }

    private boolean isWordInDB(){
        DatabaseManager db=new DatabaseManager(getApplicationContext());
        db.open();
        boolean exist=db.isWordInDB(mainWord);
        db.close();
        return exist;
    }

    public void onKeyboardButton(View view){

        char letter = keyboard.getLetterOnClick(view);
        if( letter==MyKeyboard.DELETE ) deleteLastLetter();
            else if(wordLength>=maxWordLength){
                Toast.makeText(getApplicationContext(), R.string.add_word_max_length_for_your_level,Toast.LENGTH_SHORT).show();
                MediaManager.playButtonNo();
                return;
            }
            else addLetter(letter+"");


    }


    private void addLetter(String text) {
        TextView letter=new TextView(getApplicationContext());
        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(letterSize, letterSize);
        param.setMargins(margins/2,0,margins/2,0);
        letter.setLayoutParams(param);
        letter.setBackground(getResources().getDrawable(R.drawable.word_shape));
        letter.setText(text);
        letter.setGravity(Gravity.CENTER);
        letter.setTextColor(Color.BLACK);
        letter.setTextSize(TypedValue.COMPLEX_UNIT_PX, letterSize * 11 / 16);
        spaceForLetters.addView(letter);
        wordLength++;
        mainWord+=text;
    }

    private void deleteLastLetter() {
        if(wordLength<=0) return;
        wordLength--;
        spaceForLetters.removeViewAt(wordLength);
        mainWord=mainWord.substring(0,wordLength);
    }




}
