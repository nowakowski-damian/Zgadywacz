package com.thirteendollars.guesser.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.data.CashData;
import com.thirteendollars.guesser.data.LevelData;
import com.thirteendollars.guesser.data.TimeData;
import com.thirteendollars.guesser.data.TriesData;


public class PlayActivity extends AppCompatActivity {


    private TextView timeActionBar,turnsActionBar,cashActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_play);
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);

        timeActionBar=(TextView)findViewById(R.id.guess_time_actionbar);
        turnsActionBar =(TextView)findViewById(R.id.guess_turns_actionbar);
        cashActionBar=(TextView)findViewById(R.id.guess_cash_actionbar);


        timeActionBar.setText(new TimeData().getTimePerLetterForActualLevel()+getString(R.string.per_letter_string_actionbar) );
        turnsActionBar.setText(new TriesData().getTriesPerLetterForActualLevel()+getString(R.string.per_letter_string_actionbar)  );
        cashActionBar.setText("$" + new CashData().getBalance());





    }


    @Override
    protected void onResume() {
        super.onResume();
        timeActionBar.setText(new TimeData().getTimePerLetterForActualLevel() + getString(R.string.per_letter_string_actionbar));
        turnsActionBar.setText(new TriesData().getTriesPerLetterForActualLevel() + getString(R.string.per_letter_string_actionbar));
        cashActionBar.setText("$" + new CashData().getBalance());
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }


    public void onGuess(View view) {
        startActivity( new Intent(getApplicationContext(),GuessActivity.class) );
    }

    public void onUpgrade(View view) {
        startActivity(new Intent(getApplicationContext(),UpgradeActivity.class));
    }
}


//activity_play