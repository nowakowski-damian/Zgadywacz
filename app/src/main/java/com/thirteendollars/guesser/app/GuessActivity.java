package com.thirteendollars.guesser.app;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.data.CashData;
import com.thirteendollars.guesser.data.LevelData;
import com.thirteendollars.guesser.data.StartLettersData;
import com.thirteendollars.guesser.data.TimeData;
import com.thirteendollars.guesser.data.TriesData;
import com.thirteendollars.guesser.other.LetterField;
import com.thirteendollars.guesser.other.MediaManager;
import com.thirteendollars.guesser.other.MyKeyboard;
import com.thirteendollars.guesser.wordslibrary.AndroidWord;
import com.thirteendollars.guesser.wordslibrary.DatabaseManager;

import java.util.Random;


public class GuessActivity extends AppCompatActivity {


    private final int ALL_FIELDS_CORRECT=-1;


    private PopupWindow settingsPopup,endGamePopup,abortPopup,searchingPopup;
    private LayoutInflater inflater;


    private RelativeLayout wholeView;
    private ViewGroup container;
    private LinearLayout linearLayout;
    RelativeLayout mainField;

    private String FROM_ANDROID_TXT;

    private TextView mainLengthTV,mainVictoryTV,mainLossTV;
    private TextView timeActionBar,triesActionBar,cashActionBar;
    private ViewSwitcher switcher;

    private AndroidWord aWord;
    private int chosenFieldNum;
    LetterField[] letterFields;

    CashData cashManager;
    TimeData timeManager;
    TriesData triesManager;
    StartLettersData startLettersMAnager;
    CountDownTimer timer=null;

    int wordLength;

    private int triesLeft;
    private int timeForGame;
    private boolean blockPopupCancel;
    private int statusBarHeight;






    @Override
    protected void onCreate(Bundle savedInstanceState) {



            super.onCreate(savedInstanceState);


        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(AppStaticData.FLAGS);



            setContentView(R.layout.activity_guess);
            try{
                getSupportActionBar().hide();
            }
            catch(NullPointerException actionBarException){
                Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
            }

            aWord=null;
            timer=null;
            triesLeft=0;
            blockPopupCancel=false;
            statusBarHeight=0;


            cashManager=new CashData();
            timeManager=new TimeData();
            triesManager=new TriesData();
            startLettersMAnager=new StartLettersData();

            mainLengthTV=(TextView)findViewById(R.id.guess_wlength_textview);
            mainVictoryTV=(TextView)findViewById(R.id.guess_victory_textview);
            mainLossTV=(TextView)findViewById(R.id.guess_loss_textview);
            switcher=(ViewSwitcher)findViewById(R.id.guess_switcher);

            FROM_ANDROID_TXT=getResources().getString(R.string.settings_from_android_wtype);
            inflater=getLayoutInflater();
            wholeView=(RelativeLayout)findViewById(R.id.guess_whole_view);
            initializeSettingsPopupWindow();

            timeActionBar=(TextView)findViewById(R.id.guess_time_actionbar);
            triesActionBar =(TextView)findViewById(R.id.guess_turns_actionbar);
            cashActionBar=(TextView)findViewById(R.id.guess_cash_actionbar);

            cashActionBar.setText("$" + cashManager.getBalance());
            triesActionBar.setText(triesManager.getTriesPerLetterForActualLevel() + getString(R.string.per_letter_string_actionbar));
            timeActionBar.setText(timeManager.getTimePerLetterForActualLevel() + getString(R.string.per_letter_string_actionbar));



        }


    @Override
    protected void onPause(){
        super.onPause();
        if(timer!=null) timer.cancel();
        timer=null;
        finish();
    }



    @Override
    public void onBackPressed() {

        if( endGamePopup!=null && endGamePopup.isShowing() ||
                settingsPopup!=null && settingsPopup.isShowing() ||
                searchingPopup!=null && searchingPopup.isShowing()
                ){ /*  just ignore...  */ }

        else if(timer!=null) {
            if(abortPopup==null) initializeAbortPopupWindow();
            abortPopup.showAtLocation(wholeView, Gravity.CENTER, 0, statusBarHeight);
        }

        else super.onBackPressed();

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }






    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void initializeSettingsPopupWindow() {

                           if(settingsPopup!=null && settingsPopup.isShowing()) settingsPopup.dismiss();

                            container=(ViewGroup)inflater.inflate(R.layout.settings_popup, null);
                            settingsPopup=new PopupWindow(container,
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    false
                            );
                            final ImageView wlengthDown=(ImageView) container.findViewById(R.id.settings_length_down);
                            final TextView wlengthText=(TextView) container.findViewById(R.id.settings_length_textviev);
                            final ImageView wlengthUp=(ImageView) container.findViewById(R.id.settings_length_up);
                            final TextView victoryTextView=(TextView) container.findViewById(R.id.settings_victory_textview);
                            final TextView lossTextView=(TextView) container.findViewById(R.id.settings_loss_textview);

                            //init variables
                            wordLength=AppStaticData.WORDS_LENGTH;

                            int lettOnStart=startLettersMAnager.getLettersOnStartForActualLevel();
                            int maxTries=triesManager.getTriesPerLetterForActualLevel()*wordLength - (wordLength-lettOnStart)+1;
                            int winCashTemp=CashData.countChange(wordLength, 0, maxTries);
                            int lossCashTemp=CashData.countCashFromIncorrectLetters(wordLength-1,wordLength);

                            //Init text fields
                            wlengthText.setText(wordLength+"" );
                            victoryTextView.setText(""+winCashTemp);
                            lossTextView.setText(""+lossCashTemp);
                            // Main text fields too
                            mainLengthTV.setText(""+wordLength );
                            mainVictoryTV.setText("+"+winCashTemp+"$");;
                            if(lossCashTemp<0)  mainLossTV.setText(lossCashTemp+"$");
                            else mainLossTV.setText("-"+lossCashTemp+"$");


                            wlengthDown.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                public void onClick(View v) {
                                    if (wordLength > AppStaticData.MIN_WORD_LENGTH) {

                                        wordLength--;
                                        int lettOnStart=startLettersMAnager.getLettersOnStartForActualLevel();
                                        int maxTries=triesManager.getTriesPerLetterForActualLevel()*wordLength - (wordLength-lettOnStart)+1;
                                        int winCashTemp=CashData.countChange(wordLength, 0, maxTries);
                                        int lossCashTemp=CashData.countCashFromIncorrectLetters(wordLength-1,wordLength);
                                        wlengthText.setText(Integer.toString(wordLength));
                                        victoryTextView.setText(""+winCashTemp);
                                        lossTextView.setText(""+lossCashTemp);
                                    }
                                    else MediaManager.playButtonNo();
                                }
                                                });

                            wlengthUp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if( wordLength>= new LevelData().getMaxWordLengthForCurrentLevel() ){
                                            MediaManager.playButtonNo();
                                            Toast.makeText(getApplicationContext(), R.string.max_length_for_this_level,Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        if(wordLength < AppStaticData.MAX_WORD_LENGHT_ADNROID) {

                                            wordLength++;
                                            int lettOnStart=startLettersMAnager.getLettersOnStartForActualLevel();
                                            int maxTries=triesManager.getTriesPerLetterForActualLevel()*wordLength - (wordLength-lettOnStart)+1;
                                            int winCashTemp=CashData.countChange(wordLength, 0, maxTries);
                                            int lossCashTemp=CashData.countCashFromIncorrectLetters(wordLength-1,wordLength);
                                            wlengthText.setText(Integer.toString(wordLength));
                                            victoryTextView.setText(""+winCashTemp);
                                            lossTextView.setText(""+lossCashTemp);
                                        }
                                }
                            });

                            //   CANCEL / OK BUTTONS

                            ImageView cancelButton=(ImageView) container.findViewById(R.id.settings_cancel_button);
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                        wordLength = AppStaticData.WORDS_LENGTH;
                                        int lettOnStart=startLettersMAnager.getLettersOnStartForActualLevel();
                                        int maxTries=triesManager.getTriesPerLetterForActualLevel()*wordLength - (wordLength-lettOnStart)+1;
                                        int winCashTemp=CashData.countChange(wordLength, 0, maxTries);
                                        int lossCashTemp=CashData.countCashFromIncorrectLetters(wordLength - 1, wordLength);
                                        wlengthText.setText(Integer.toString(wordLength));
                                        victoryTextView.setText(""+winCashTemp);
                                        lossTextView.setText(""+lossCashTemp);
                                        settingsPopup.dismiss();
                                        if(blockPopupCancel) finish();

                                }
                            });

                            ImageView okButton=(ImageView) container.findViewById(R.id.settings_ok_button);
                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v){

                                        //No more words with this length
                                        AppStaticData.WORDS_LENGTH = wordLength;
                                        AppStaticData.saveSettingsToSP();
                                        int lettOnStart=startLettersMAnager.getLettersOnStartForActualLevel();
                                        int maxTries=triesManager.getTriesPerLetterForActualLevel()*wordLength - (wordLength-lettOnStart)+1;
                                        int winCashTemp=CashData.countChange(wordLength, 0, maxTries);
                                        int lossCashTemp=CashData.countCashFromIncorrectLetters(wordLength-1,wordLength);
                                        mainLengthTV.setText(Integer.toString(wordLength));
                                        mainVictoryTV.setText("+"+winCashTemp+"$");;
                                        if(lossCashTemp<0)  mainLossTV.setText(lossCashTemp+"$");
                                        else mainLossTV.setText("-"+lossCashTemp+"$");
                                        settingsPopup.dismiss();
                                        if(blockPopupCancel) {
                                            onStartGuess(null);
                                            blockPopupCancel=false;
                                        }

                                    }
                            });

                        fullScreenImmersive(settingsPopup.getContentView());

    }




    private void initializeEndGamePopupWindow(int correct,int incorrect) {


        if(endGamePopup!=null && endGamePopup.isShowing()) endGamePopup.dismiss();


        container=(ViewGroup)inflater.inflate(R.layout.end_game_popup, null);
        endGamePopup=new PopupWindow(container,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                false
        );

        final TextView winOrLossText=(TextView) container.findViewById(R.id.end_popup_win_or_loss_textview);
        final TextView correctWordsText=(TextView) container.findViewById(R.id.end_popup_corect_words_textview);
        final TextView correctCashText=(TextView) container.findViewById(R.id.end_popup_corect_cash_textview);
        final TextView incorrectWordsText=(TextView) container.findViewById(R.id.end_popup_incorect_words_textview);
        final TextView incorrectCashText=(TextView) container.findViewById(R.id.end_popup_incorect_cash_textview);
        final TextView winBonusCashText=(TextView) container.findViewById(R.id.end_popup_winbonus_cash_textview);
        final TextView triesBonusText=(TextView) container.findViewById(R.id.end_popup_tries_bonus_textview);
        final TextView sumCashText=(TextView) container.findViewById(R.id.end_popup_sum_cash_textview);

        final TextView playAgainButton=(TextView) container.findViewById(R.id.end_popup_trynextword_button);
        final TextView settingsButton=(TextView) container.findViewById(R.id.end_popup_settings_button);
        final TextView mainMenuButton=(TextView) container.findViewById(R.id.end_popup_mainmenu_button);




        //Init text fields
        if(incorrect==0) {
            winOrLossText.setTextColor(getResources().getColor(R.color.green));
            winOrLossText.setText(getResources().getString(R.string.end_popup_youwin));
        }
        else  {
            winOrLossText.setTextColor(getResources().getColor(R.color.red));
            winOrLossText.setText(getResources().getString(R.string.end_popup_youloss));
        }

        correctWordsText.setText(Integer.toString(correct));
        correctCashText.setText("+"+Integer.toString(CashData.countCashFromCorrectLetters(correct)));

        incorrectWordsText.setText(Integer.toString(incorrect));
        if(incorrect>0)
            incorrectCashText.setText(Integer.toString(CashData.countCashFromIncorrectLetters(incorrect,correct+incorrect)));

        if(incorrect==0) {
            triesBonusText.setText("+"+ CashData.countCashFromTriesBonus(triesLeft, correct + incorrect) );
            winBonusCashText.setText("+" + Integer.toString(CashData.countCashFromWinBonus(correct + incorrect)));
        }

        int sum = CashData.countChange(correct,incorrect,triesLeft);
        if(sum>=0) sumCashText.setText("+"+Integer.toString(sum));
        else  sumCashText.setText(Integer.toString(sum));



        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGamePopup.dismiss();
                onStartGuess(null);


            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSettings(null);

            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGamePopup.dismiss();
                finish();

            }
        });
        fullScreenImmersive(endGamePopup.getContentView());
    }

    private void initializeAbortPopupWindow() {

        if(abortPopup!=null && abortPopup.isShowing()) abortPopup.dismiss();

        container=(ViewGroup)inflater.inflate(R.layout.confirm_exit_popup, null);
        abortPopup=new PopupWindow(container,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                false
        );

        final TextView yes=(TextView) container.findViewById(R.id.abort_yes_button);
        final TextView no=(TextView) container.findViewById(R.id.abort_no_button);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abortPopup.dismiss();
                int correct=getNumOfCorrectsFields();
                loss(correct,AppStaticData.WORDS_LENGTH - correct);

            }
        });

       no.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               abortPopup.dismiss();
           }
       });
    }

    private void initializeSearchingPopupWindow() {
        if(searchingPopup!=null && searchingPopup.isShowing()) searchingPopup.dismiss();
        container=(ViewGroup)inflater.inflate(R.layout.searching_popup, null);
        searchingPopup=new PopupWindow(container,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                false
        );
        fullScreenImmersive(searchingPopup.getContentView());

    }




    public void onSettings(View view) {
        settingsPopup.showAtLocation(wholeView, Gravity.FILL_VERTICAL, 0, statusBarHeight);
    }





    public void onStartGuess(View view) {

        initializeSearchingPopupWindow();
        searchingPopup.showAtLocation(wholeView, Gravity.FILL_VERTICAL, 0, statusBarHeight);

                        setTimeAndTriesForWord(AppStaticData.WORDS_LENGTH);
                        loadWordFromAndroidDB(wordLength);

    }


    private void wordFound(){

        if(aWord==null){
            blockPopupCancel=true;
            Toast.makeText(getApplicationContext(), R.string.no_more_words_toast,Toast.LENGTH_LONG).show();
            if(searchingPopup!=null && searchingPopup.isShowing()) searchingPopup.dismiss();
            settingsPopup.showAtLocation(wholeView, Gravity.FILL_VERTICAL, 0, statusBarHeight);
            return;
        }

        MediaManager.playTournamentMusic();
        blockPopupCancel=false;

        letterFields=new LetterField[wordLength];
        removeLetterView();
        inflater.inflate(R.layout.guess_play_view, mainField);
        LinearLayout letterSpace=(LinearLayout)findViewById(R.id.guess_letters_space);
        linearLayout=new LinearLayout(getApplicationContext() );

        // Setting up TextView dimensions
        int margins= AppStaticData.screenDensity/20;
        if(wordLength>20)margins=2;
        else if (wordLength>15) margins/=2;
        else if(wordLength>10) margins*=2./3;
        int letterSize= AppStaticData.screenHeight /  wordLength;
        letterSize-=margins*2;
        int maxSize=AppStaticData.screenWidth/6;
        if(letterSize>maxSize) letterSize=maxSize;


        //Creating new view


        for(int i=0 ;i<wordLength;i++) {

            TextView text=new TextView(getApplicationContext());
            text.setTextColor(Color.BLACK);
            text.setBackgroundResource(R.drawable.word_shape);
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, letterSize * 11 / 16);
            text.setGravity(Gravity.CENTER);
            text.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(letterSize, letterSize);
            param.setMargins(margins, 0, 0, 0);
            text.setLayoutParams(param);

            int letterId=View.generateViewId();
            text.setId(letterId);
            letterFields[i]=new LetterField(letterId,aWord.getWord().charAt(i));

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLetter(v, letterFields);

                }


            });

            linearLayout.addView(text);
        }

        letterSpace.addView(linearLayout);
        fillFieldsByStartingLetters();
        chosenFieldNum=getFirstIncorrectFieldNum(0);
        if(chosenFieldNum==ALL_FIELDS_CORRECT) victory();
        else setChosenFieldBackground(chosenFieldNum);
        startCountingDown();
        if(searchingPopup!=null && searchingPopup.isShowing()) searchingPopup.dismiss();

    }

    private void removeLetterView(){
        mainField= (RelativeLayout) findViewById(R.id.guess_main_field);
        mainField.removeAllViewsInLayout();
    }

    private void startCountingDown() {

        timer=new CountDownTimer(timeForGame*1000,1000) {

            public void onTick(long millisUntilFinished) {

                if ( millisUntilFinished/1000 == 10 ) timeActionBar.setTextColor(getResources().getColor(R.color.red));
                timeActionBar.setText(millisUntilFinished/1000+"");
            }

            public void onFinish() {
                timeActionBar.setText("0");
                int correctFields= getNumOfCorrectsFields();
                if(correctFields>=letterFields.length) victory();
                else loss(correctFields,letterFields.length-correctFields);
            }

        };
        timer.start();
    }

    private void fillFieldsByStartingLetters() {

        int numOfStartLetters=startLettersMAnager.getLettersOnStartForActualLevel();
        if(numOfStartLetters>letterFields.length) numOfStartLetters=letterFields.length;
        int[] letterFieldNums=new int[numOfStartLetters];
        Random rand = new Random();


        for(int i=0;i<numOfStartLetters;i++){
        letterFieldNums[i]=rand.nextInt(letterFields.length);
        for(int j=0;j<i;j++) if(letterFieldNums[j]==letterFieldNums[i]){ i--; break;}
        }

        for(int i=0;i<numOfStartLetters;i++) {
            ((TextView) mainField.findViewById(letterFields[ letterFieldNums[i] ].getId())).setText(letterFields[ letterFieldNums[i] ].getLetter());
            setCorrectFieldBackground(letterFieldNums[i]);
        }
    }





    private void loadWordFromAndroidDB(int wordLength) {
        try {
            aWord = DatabaseManager.getInstance(this).getAndroidWord(wordLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wordFound();
    }



    private void onLetter(View v,LetterField[] letterFields) {

        //find which num has chosen field
        int num;
        for(num=0;num<letterFields.length;num++) if(v.getId()==letterFields[num].getId()) break;
        chosenFieldNum=num;



        for(int i=0;i<letterFields.length;i++) {
            //nothing inside
            if( getTextFromTextView( letterFields[i].getId() ).length() <1 ) setEmptyFieldBackground(i);
            //correct
            else if( letterFields[i].isWordCorrect(   getTextFromTextView( letterFields[i].getId() )   )  ) setCorrectFieldBackground(i);
            //incorrect
            else setIncorrectFieldBackground(i);
        }
        v.setBackgroundResource(R.drawable.word_shape_chosen);

    }



    private void setEmptyFieldBackground(int which){
        findViewById(letterFields[which].getId()).setBackgroundResource(R.drawable.word_shape);
    }

    private void setChosenFieldBackground(int which){
        findViewById(letterFields[which].getId()).setBackgroundResource(R.drawable.word_shape_chosen);
    }

    private void setCorrectFieldBackground(int which){
        findViewById(letterFields[which].getId()).setBackgroundResource(R.drawable.word_shape_correct);
    }

    private void setIncorrectFieldBackground(int which){
        findViewById(letterFields[which].getId()).setBackgroundResource(R.drawable.word_shape_incorrect);
    }


    private String getTextFromTextView(int viewId){
       return ((TextView)findViewById(viewId)).getText().toString();
    }


    private int getFirstIncorrectFieldNum(int currentField){
        int num;
        String text;

        for(num=currentField;num<letterFields.length;num++){
            text= getTextFromTextView( letterFields[num].getId() );
            if(text.length() <1 || ! letterFields[num].isWordCorrect(text) ) return num;
        }

        for(num=0;num<currentField;num++){
            text= getTextFromTextView( letterFields[num].getId() );
            if(text.length() <1 || ! letterFields[num].isWordCorrect(text) ) return num;
        }

        return ALL_FIELDS_CORRECT;
    }



    private void goToNextField(int currentField){
        setCorrectFieldBackground(currentField);
        int nextField=getFirstIncorrectFieldNum(currentField);

        if(nextField==ALL_FIELDS_CORRECT)   victory();
        else{
            chosenFieldNum=nextField;
            setChosenFieldBackground(chosenFieldNum);
        }
    }

    private int getNumOfCorrectsFields(){
    int num=0;
        for(int i=0;i<letterFields.length;i++) if(letterFields[i].isWordCorrect(getTextFromTextView(letterFields[i].getId()))) num++;
        return num;
    }




    public void onKeyboardButton(View view) {

        if(endGamePopup!=null && endGamePopup.isShowing()) return;

        char letter=new MyKeyboard().getLetterOnClick(view);

        if(letter==MyKeyboard.DELETE){
            ((TextView)mainField.findViewById(letterFields[chosenFieldNum].getId())).setText("");
            return;
        }
        else ((TextView)mainField.findViewById(letterFields[chosenFieldNum].getId())).setText(letter+"");
        if(letterFields[chosenFieldNum].isWordCorrect(letter+"")) goToNextField(chosenFieldNum);

        triesLeft--;
        triesActionBar.setText(triesLeft+"");
        if(triesLeft==3) triesActionBar.setTextColor(getResources().getColor(R.color.red));
        if(triesLeft<=0) {
            int numOfCorrect=getNumOfCorrectsFields();
            loss(numOfCorrect,letterFields.length-numOfCorrect);
        }

    }




    private void setTimeAndTriesForWord(int length) {
        timeActionBar.setTextColor(getResources().getColor(R.color.white));
        triesActionBar.setTextColor(getResources().getColor(R.color.white));
        timeForGame= timeManager.getTimePerLetterForActualLevel() * length;
        triesLeft= triesManager.getTriesPerLetterForActualLevel() *length;
        triesActionBar.setText(triesLeft + "");
        timeActionBar.setText(timeForGame+"");
    }



    private void victory(){

        MediaManager.stopTournamentMusic();
        MediaManager.playEndgame();
        DatabaseManager.getInstance(this).setWordGuessed(aWord);

        if(timer!=null) timer.cancel();
        initializeEndGamePopupWindow(letterFields.length, 0);
        endGamePopup.showAtLocation(wholeView, Gravity.FILL_VERTICAL, 0, statusBarHeight);
        cashManager.changeBalance(CashData.countChange(letterFields.length, 0, triesLeft));
        cashActionBar.setText("$" + cashManager.getBalance());
        aWord=null;
    }

    private void loss(int correct,int incorrect){
        MediaManager.stopTournamentMusic();
        MediaManager.playEndgame();
        if(timer != null) timer.cancel();
        if( abortPopup!=null && abortPopup.isShowing() ) abortPopup.dismiss();
        initializeEndGamePopupWindow(correct, incorrect);
        endGamePopup.showAtLocation(wholeView, Gravity.FILL_VERTICAL, 0, statusBarHeight);
        cashManager.changeBalance(CashData.countChange(correct, incorrect, triesLeft));
        cashActionBar.setText("$" + cashManager.getBalance());
        aWord=null;
    }

    public void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }

}

