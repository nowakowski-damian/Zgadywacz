package com.thirteendollars.guesser.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.data.CashData;
import com.thirteendollars.guesser.data.LevelData;
import com.thirteendollars.guesser.other.MediaManager;

import java.util.List;

public class CreateActivity extends AppCompatActivity {


    int numOfOwnWords;
    int maxNumOfWords;
    int incomesNum;

    LinearLayout freeSpace;
    View wordRecordView;
    LayoutInflater inflater;
    TextView incomesTv,wordsNumberTV,lengthLimitTv,wordsLimitTv;
    int wordsId[];
    boolean isDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
        }
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);


        wordsNumberTV=(TextView)findViewById(R.id.create_words_number);
        lengthLimitTv=(TextView)findViewById(R.id.create_length_limit);
        wordsLimitTv=(TextView)findViewById(R.id.create_words_limit);
        incomesTv=(TextView)findViewById(R.id.create_incomes_only);

        lengthLimitTv.setText( getString(R.string.create_length_limit) + new LevelData().getMaxWordLengthForCurrentLevel() );
        maxNumOfWords= new LevelData().getMaxOwnWordsForCurrentLevel();
        wordsLimitTv.setText(getString(R.string.create_words_limit) + maxNumOfWords);


        //init views creators
        freeSpace=(LinearLayout)findViewById(R.id.space_for_own_word_records);
        inflater=getLayoutInflater();
    }


    @Override
    protected void onResume(){
        super.onResume();
        isDone=true;
        incomesNum=ParseUser.getCurrentUser().getInt("incomes");
        incomesTv.setText(CashData.countCashFromIncomes(incomesNum) + "$");
        freeSpace.removeAllViewsInLayout();
        freeSpace.invalidate();
        createWordList();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }



    private void createWordList() {
        isDone=false;
        // setting list of words
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserWords");
        query.whereEqualTo("ownerId", AppStaticData.pUser.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> wordsList, ParseException e) {
                if (e == null) {
                    numOfOwnWords = wordsList.size();
                    saveNumOfWords(numOfOwnWords);
                    //if num of words is too large
                    if (numOfOwnWords > maxNumOfWords) {
                        for (int i = maxNumOfWords; i < numOfOwnWords; i++)
                            wordsList.get(i).deleteInBackground();
                        numOfOwnWords = maxNumOfWords;
                        saveNumOfWords(numOfOwnWords);
                    }

                    wordsId = new int[numOfOwnWords];

                    for (int i = 0; i < numOfOwnWords; i++) {
                        wordRecordView = inflater.inflate(R.layout.create_word_own_record, null);
                        ((TextView) wordRecordView.findViewById(R.id.word_record_number)).setText(i + 1 + ".");
                        ((TextView) wordRecordView.findViewById(R.id.word_record_name)).setText(wordsList.get(i).getString("word").toUpperCase());
                        ((TextView) wordRecordView.findViewById(R.id.word_record_casualties)).setText(wordsList.get(i).getInt("casualties") + "");
                        wordsId[i] = wordRecordView.generateViewId();
                        wordRecordView.findViewById(R.id.word_record_delete_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String strNum = ((TextView) ((LinearLayout) v.getParent()).findViewById(R.id.word_record_number)).getText().toString();
                                if (strNum.length() > 2) strNum = strNum.substring(0, 2);
                                else strNum = strNum.substring(0, 1);
                                int num = Integer.parseInt(strNum);
                                num--;
                                try {
                                    wordsList.get(num).delete();
                                    freeSpace.removeAllViewsInLayout();
                                    freeSpace.invalidate();
                                    numOfOwnWords--;
                                    createWordList();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                    wordsList.get(num).deleteInBackground();
                                    Toast.makeText(getApplicationContext(), R.string.add_word_delete_soon, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        freeSpace.addView(wordRecordView);
                    }

                } else {
                    TextView txt = new TextView(getApplicationContext());
                    txt.setText(R.string.create_word_list_connection_problem);
                    txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                    txt.setGravity(Gravity.CENTER);
                    txt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    freeSpace.addView(txt);
                }
                wordsNumberTV.setText(getString(R.string.create_words_number) + getNumOfOwnWords());
                isDone = true;
            }

        });

    }


    private void saveNumOfWords(int numOfOwnWords) {
        AppStaticData.spEditor.putInt("savedNumberOfOwnWords",numOfOwnWords);
        AppStaticData.spEditor.commit();
    }

    private int getNumOfOwnWords(){
        return AppStaticData.sp.getInt("savedNumberOfOwnWords",0);
    }


    public void onAddWord(View view) {
        if(numOfOwnWords<maxNumOfWords) startActivity(new Intent(getApplicationContext(),AddWordActivity.class));
        else {
            Toast.makeText(getApplicationContext(), R.string.add_word_you_achieved_limit, Toast.LENGTH_SHORT).show();
            MediaManager.playButtonNo();
        }
    }

    public void onDrawOut(View view) {
        if(incomesNum>0) {
            ParseUser.getCurrentUser().increment("incomes", -incomesNum);
            ParseUser.getCurrentUser().saveInBackground();
            new CashData().changeBalance(CashData.countCashFromIncomes(incomesNum));
            incomesNum = 0;
            incomesTv.setText(CashData.countCashFromIncomes(incomesNum) + "$");
            MediaManager.playEndgame();
            Toast.makeText(getApplicationContext(), R.string.on_draw_out,Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.on_draw_out, Toast.LENGTH_SHORT).show();
            MediaManager.playButtonNo();
        }
    }

    public void onRefresh(View view) {
       if(isDone) {
           freeSpace.removeAllViewsInLayout();
           freeSpace.invalidate();
           createWordList();
       }
    }
}
