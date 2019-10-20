package com.thirteendollars.guesser.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.data.CashData;
import com.thirteendollars.guesser.data.LevelData;
import com.thirteendollars.guesser.data.StartLettersData;
import com.thirteendollars.guesser.data.TimeData;
import com.thirteendollars.guesser.data.TriesData;
import com.thirteendollars.guesser.other.MediaManager;

public class UpgradeActivity extends AppCompatActivity {


    //Views from main view
    private TextView triesCurrentLevel;
    private TextView triesCurrentTriesPerLetter;

    private TextView timeCurrentLevel;
    private TextView timeCurrentTimePerLetter;

    private TextView startLettersCurrentLevel;
    private TextView startLettersCurrentStartLettersPerLetter;

    private TextView levelCurrentLevel;
    private TextView levelCurrentLengthAndOwnWords;

    private TextView cashBalance;

    //Views from popup
    private ImageView pImage;
    private TextView pMainTittle;
    private TextView pDefinitionBottom;
    private TextView pDefinitionUpper;
    private TextView pCost;
    private TextView pGetUpgrade;
    
    private PopupWindow popup;
    private ViewGroup container;
    private LayoutInflater inflater;
    private RelativeLayout wholeView;


    //Data Managers
    private TriesData triesManager;
    private TimeData timeManager;
    private StartLettersData startLettersManager;
    private LevelData levelManager;
    private CashData cashManager;
    private int statusBarHeight;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
        }
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
        statusBarHeight=getStatusBarHeight();

        //Views from main view
        triesCurrentLevel=(TextView)findViewById(R.id.more_tries_current_level_textview);
        triesCurrentTriesPerLetter=(TextView)findViewById(R.id.more_tries_current_tries_p_letter_textview);
        timeCurrentLevel=(TextView)findViewById(R.id.more_time_current_level_textview);
        timeCurrentTimePerLetter=(TextView)findViewById(R.id.more_time_current_time_p_letter_textview);
        startLettersCurrentLevel=(TextView)findViewById(R.id.more_letters_current_level_textview);
        startLettersCurrentStartLettersPerLetter=(TextView)findViewById(R.id.more_letters_current_start_letters_textview);
        levelCurrentLevel= (TextView)findViewById(R.id.level_current_level_textview);
        levelCurrentLengthAndOwnWords=(TextView)findViewById(R.id.level_current_length_and_ownwords_textview);
        cashBalance=(TextView)findViewById(R.id.upgrade_cash_textview);
        //Views from popup
        inflater=getLayoutInflater();
        wholeView=(RelativeLayout)findViewById(R.id.upgrade_main_view);
        initializeUpgradePopup(); // to use container
        pImage=(ImageView)container.findViewById(R.id.upgrade_popup_image);
        pMainTittle=(TextView)container.findViewById(R.id.upgrade_popup_main_tittle);
        pDefinitionBottom=(TextView)container.findViewById(R.id.upgrade_popup_defbottom_textview);
        pDefinitionUpper=(TextView)container.findViewById(R.id.upgrade_popup_defupper_textview);
        pCost=(TextView)container.findViewById(R.id.upgrade_popup_cost_textview);
        pGetUpgrade=(TextView)container.findViewById(R.id.upgrade_popup_get_upgrade_button);
        //Data Managers
        triesManager=new TriesData();
        timeManager=new TimeData();
        startLettersManager=new StartLettersData();
        levelManager=new LevelData();
        cashManager=new CashData();

        updateAllTextVievs();

    }

    @Override
    public void onBackPressed() {
        if(popup!=null && popup.isShowing()) popup.dismiss();
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


    private void initializeUpgradePopup() {
        if (popup != null && popup.isShowing()) popup.dismiss();
        container = (ViewGroup) inflater.inflate(R.layout.upgrade_popup, null);
        popup = new PopupWindow(container, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popup.setFocusable(false);
        popup.setBackgroundDrawable(new ColorDrawable());
    }

    private void prepareTimePopup() {

        final int nextLevel=timeManager.getTimeLevel()+1;
        final int minGameLevel=timeManager.getMinLevel(nextLevel);
        final int cost=timeManager.getCost(nextLevel);

        pMainTittle.setText( getString(R.string.upgrade_popup_time)+" "+nextLevel+" "+getString(R.string.upgrade_popup_level_small));
        pImage.setBackground(getResources().getDrawable(R.drawable.more_time));
        pDefinitionUpper.setText(getString(R.string.upgrade_popup_time_p_letter)+" " + timeManager.getTimePerLetter(nextLevel)+"s");
        pDefinitionBottom.setText(getString(R.string.upgrade_popup_min_game_level)+" " + minGameLevel);
        pCost.setText(getString(R.string.upgrade_popup_cost) + cost);

        pGetUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (minGameLevel > levelManager.getLevel()) {
                    showLevelMssg();
                    MediaManager.playButtonNo();
                } else if (cost > cashManager.getBalance()) {
                    showMoneyMssg();
                    MediaManager.playButtonNo();
                } else {
                    cashManager.changeBalance(-cost);
                    timeManager.increase();
                    updateTimeTextView();
                    updateCashBalanceTextView();
                    popup.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.after_upgrade_text, Toast.LENGTH_SHORT).show();
                    MediaManager.playUpgrade();;
                }
            }
        });
    }

    private void prepareTriesPopup() {

        final int nextLevel=triesManager.getTriesLevel()+1;
        final int minGameLevel=triesManager.getMinLevel(nextLevel);
        final int cost=triesManager.getCost(nextLevel);

        pMainTittle.setText( getString(R.string.upgrade_popup_tries_tittle)+" "+nextLevel+" "+getString(R.string.upgrade_popup_level_small));
        pImage.setBackground(getResources().getDrawable(R.drawable.more_tries));
        pDefinitionUpper.setText(getString(R.string.upgrade_popup_tries_per_letter)+" " + triesManager.getTriesPerLetter(nextLevel));
        pDefinitionBottom.setText(getString(R.string.upgrade_popup_min_game_level)+" " + minGameLevel);
        pCost.setText(getString(R.string.upgrade_popup_cost)+cost );

        pGetUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( minGameLevel> levelManager.getLevel() ){
                    showLevelMssg();
                    MediaManager.playButtonNo();
                }
                else if( cost> cashManager.getBalance() ) {
                    showMoneyMssg();
                    MediaManager.playButtonNo();
                }
                else{
                    cashManager.changeBalance(-cost);
                    triesManager.increase();
                    updateTriesTextView();
                    updateCashBalanceTextView();
                    popup.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.after_upgrade_text,Toast.LENGTH_SHORT).show();
                    MediaManager.playUpgrade();;
                }
            }
        });

    }

    private void prepareStartLettersPopup() {
        final int nextLevel=startLettersManager.getStartLettersLevel()+1;
        final int minGameLevel=startLettersManager.getMinLevel(nextLevel);
        final int cost=startLettersManager.getCost(nextLevel);

        pMainTittle.setText( getString(R.string.upgrade_popup_start_letters_title)+" "+nextLevel+" "+getString(R.string.upgrade_popup_level_small));
        pImage.setBackground(getResources().getDrawable(R.drawable.more_letter));
        pDefinitionUpper.setText(getString(R.string.upgrade_popup_letters_on_beginning)+" " + startLettersManager.getLettersOnStart(nextLevel));
        pDefinitionBottom.setText(getString(R.string.upgrade_popup_min_game_level)+" " + minGameLevel);
        pCost.setText(getString(R.string.upgrade_popup_cost)+cost );

        pGetUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( minGameLevel> levelManager.getLevel() ) {
                    showLevelMssg();
                    MediaManager.playButtonNo();
                }
                else if( cost> cashManager.getBalance() ) {
                    showMoneyMssg();
                    MediaManager.playButtonNo();
                }
                else{
                    cashManager.changeBalance(-cost);
                    startLettersManager.increase();
                    updateStartLettersTextView();
                    updateCashBalanceTextView();
                    popup.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.after_upgrade_text,Toast.LENGTH_SHORT).show();
                    MediaManager.playUpgrade();;
                }
            }
        });
    }

    private void prepareLevelPopup() {
        final int nextLevel=levelManager.getLevel()+1;
        final int cost=levelManager.getCost(nextLevel);

        pMainTittle.setText( getString(R.string.upgrade_popup_game_level_tittle)+" "+nextLevel+" "+getString(R.string.upgrade_popup_level_small));
        pImage.setBackground(getResources().getDrawable(R.drawable.level_up) );
        pDefinitionUpper.setText(getString(R.string.upgrade_popup_max_word_lenth)+" " + levelManager.getMaxWordLength(nextLevel) );
        pDefinitionBottom.setText(getString(R.string.upgrade_popup_max_own_words)+" " + levelManager.getMaxOwnWords(nextLevel) );
        pCost.setText(getString(R.string.upgrade_popup_cost)+cost );

        pGetUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if( cost> cashManager.getBalance() ) {
                   showMoneyMssg();
                   MediaManager.playButtonNo();
               }
                else{
                    cashManager.changeBalance(-cost);
                   levelManager.increase();
                   updateLevelTextView();
                    updateCashBalanceTextView();
                    popup.dismiss();
                   Toast.makeText(getApplicationContext(), R.string.after_upgrade_text,Toast.LENGTH_SHORT).show();
                   MediaManager.playUpgrade();;
                }
            }
        });
    }



    private void updateAllTextVievs() {
        updateTriesTextView();
        updateTimeTextView();
        updateStartLettersTextView();
        updateLevelTextView();
        updateCashBalanceTextView();
    }

    private void updateCashBalanceTextView() {
        cashBalance.setText( "$"+cashManager.getBalance() );
    }

    private void updateTriesTextView() {
        triesCurrentLevel.setText(getString(R.string.level) + " " + triesManager.getTriesLevel() + "/25");
        triesCurrentTriesPerLetter.setText("( " + triesManager.getTriesPerLetterForActualLevel() + "/" + getString(R.string.letter) + " )");
    }

    private void updateTimeTextView() {
        timeCurrentLevel.setText( getString(R.string.level)+" "+timeManager.getTimeLevel()+"/25" );
        timeCurrentTimePerLetter.setText("( "+timeManager.getTimePerLetterForActualLevel()+"s/"+getString(R.string.letter)+" )" );
    }

    private void updateStartLettersTextView() {
        startLettersCurrentLevel.setText( getString(R.string.level)+" "+startLettersManager.getStartLettersLevel()+"/5" );
        startLettersCurrentStartLettersPerLetter.setText("( "+startLettersManager.getLettersOnStartForActualLevel()+" "+getString(R.string.letters)+" )" );
    }

    private void updateLevelTextView() {
        levelCurrentLevel.setText( getString(R.string.level)+" "+ levelManager.getLevel()+"/25");
        levelCurrentLengthAndOwnWords.setText(getString(R.string.max_length)+ levelManager.getMaxWordLengthForCurrentLevel()+" "+getString(R.string.own_words)+levelManager.getMaxOwnWordsForCurrentLevel() );
    }





    public void onTriesUp(View view) {
        if( triesManager.getTriesLevel() >= AppStaticData.MAX_TRIES_LEVEL ) Toast.makeText(getApplicationContext(), R.string.max_level_achieved_warning, Toast.LENGTH_SHORT).show();
        else{
            prepareTriesPopup();
            fullScreenImmersive(popup.getContentView());
            popup.showAtLocation(wholeView, Gravity.NO_GRAVITY, 0, statusBarHeight);
        }
    }


    public void onTimeUp(View view) {
        if( timeManager.getTimeLevel() >= AppStaticData.MAX_TIME_LEVEL ) Toast.makeText(getApplicationContext(), R.string.max_level_achieved_warning, Toast.LENGTH_SHORT).show();
        else{
            prepareTimePopup();
            fullScreenImmersive(popup.getContentView());
            popup.showAtLocation(wholeView, Gravity.NO_GRAVITY, 0, statusBarHeight);
        }
    }

    public void onStartLettersUp(View view) {
        if( startLettersManager.getStartLettersLevel() >= AppStaticData.MAX_START_LETTERS_LEVEL ) Toast.makeText(getApplicationContext(), R.string.max_level_achieved_warning, Toast.LENGTH_SHORT).show();
        else{
            prepareStartLettersPopup();
            fullScreenImmersive(popup.getContentView());
            popup.showAtLocation(wholeView, Gravity.NO_GRAVITY, 0, statusBarHeight);
        }
    }

    public void onLevelUp(View view) {
        if( levelManager.getLevel() >= AppStaticData.MAX_GAME_LEVEL ) Toast.makeText(getApplicationContext(), R.string.max_level_achieved_warning, Toast.LENGTH_SHORT).show();
        else{
            prepareLevelPopup();
            fullScreenImmersive(popup.getContentView());
            popup.showAtLocation(wholeView, Gravity.NO_GRAVITY, 0, statusBarHeight);
        }
    }
    
    


    
    private void showMoneyMssg(){
        Toast.makeText(getApplicationContext(),getString(R.string.upgrade_not_enough_money)+" "+cashManager.getBalance(),Toast.LENGTH_SHORT).show();
    }

    private void showLevelMssg(){
        Toast.makeText(getApplicationContext(),getString(R.string.upgrade_game_level_too_low)+" "+levelManager.getLevel(),Toast.LENGTH_SHORT).show();
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
