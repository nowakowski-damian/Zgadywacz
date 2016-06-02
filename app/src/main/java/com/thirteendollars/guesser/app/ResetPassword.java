package com.thirteendollars.guesser.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.thirteendollars.guesser.R;

public class ResetPassword extends AppCompatActivity {

    TextView email;
    ViewSwitcher switcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getSupportActionBar().setTitle(R.string.reset_activity_actionbar_tittle);
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException,Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_reset_password);
        email=(TextView)findViewById(R.id.reset_email_edittext);
        switcher=(ViewSwitcher)findViewById(R.id.resetViewSwitcher);
    }


    public void onReset(View view) {

        String emailText= email.getText().toString();
        if( isEmailCorrect(emailText) ) {
            switcher.showNext();

            ParseUser.requestPasswordResetInBackground(emailText, new RequestPasswordResetCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), R.string.reset_succes_information,Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        switcher.showPrevious();
                        Toast.makeText(getApplicationContext(),getErrorMessage(e.getCode()),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }


    private boolean isEmailCorrect(String email){

        String warning="";
        boolean isCorrect=true;

        if( email.isEmpty() ){
            warning+=getText(R.string.warning_emailEmpty).toString();
            isCorrect=false;
        }
        else {

            if (email.length() < 6) {
                warning += getText(R.string.warning_mailLenghtmin).toString();
                isCorrect = false;
            }

            if (email.charAt(0) < 'A' || email.charAt(0) > 'z') {
                warning += getText(R.string.warning_emailBegin).toString();
                isCorrect = false;
            }

            if (email.contains(" ")) {
                warning += getText(R.string.warning_emailSpace).toString();
                isCorrect = false;
            }


            if (!email.contains("@")) {
                warning += getText(R.string.warning_emailAt).toString();
                isCorrect = false;
            }
        }


        if( !isCorrect ){
            Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_LONG).show();
        }

        return isCorrect;

    }


    private String getErrorMessage(int code){

        switch(code){
            case 125: return getString(R.string.email_invalid);
            case 100: return getString(R.string.connection_problems);
        }
        return  getString(R.string.unknown_error);
    }




}
