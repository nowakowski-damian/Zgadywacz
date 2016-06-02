package com.thirteendollars.guesser.app;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.thirteendollars.guesser.R;


public class SignupActivity extends AppCompatActivity {

    String nick;
    String email;
    String password;

    EditText nickField;
    EditText emailField;
    EditText passwordField;
    EditText passwordConfField;
    ViewSwitcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        switcher=(ViewSwitcher)findViewById(R.id.signUpViewSwitcher);
        nickField=(EditText)findViewById(R.id.signup_nick_editText);
        emailField=(EditText)findViewById(R.id.signup_email_editText);
        passwordField=(EditText)findViewById(R.id.signup_passw_EditText);
        passwordConfField=(EditText)findViewById(R.id.signup_passw_confirm_EditText);

    }


    public void onSignMeUpFinally(View view) {

        nick=nickField.getText().toString();
        email=emailField.getText().toString();
        password=passwordField.getText().toString();

        if ( isDataCorrect() ){
            switcher.showNext();
            ParseUser user = new ParseUser();
            user.setUsername(nick);
            user.setPassword(password);
            user.setEmail(email);


            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(),getText(R.string.signedUpSuccess).toString(),Toast.LENGTH_LONG).show();

                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),getErrorMessageS( e.getCode() ),Toast.LENGTH_LONG).show();
                        switcher.showPrevious();
                    }
                }
            });



        }
        else return;
    }

    private String getErrorMessageS(int code){

        switch(code){
            case 202: return getString(R.string.usernale_already_taken);
            case 203: return getString(R.string.email_already_taken);
            case 125: return getString(R.string.email_invalid);
            case 100: return getString(R.string.connection_problems);
        }
        return  getString(R.string.unknown_error);
    }




    private boolean isDataCorrect() {

        String warning= getText(R.string.warning_template).toString();
        boolean isCorrect = true;


        //NICK
        if( nick.isEmpty() ){
            warning+=getText(R.string.warning_nickEmpty).toString();
            isCorrect=false;
        }
        else {

            if (nick.length() < 3) {
                warning += getText(R.string.warning_nickLenghtmin).toString();
                isCorrect = false;
            }

            if (nick.length() > 20) {
                warning += getText(R.string.warning_nickLenghtmax).toString();
                isCorrect = false;
            }

            if (nick.contains(" ")) {
                warning += getText(R.string.warning_nickSpace).toString();
                isCorrect = false;
            }

            if (nick.charAt(0) < 'A' || nick.charAt(0) > 'z') {
                warning += getText(R.string.warning_nickBegin).toString();
                isCorrect = false;
            }
        }

        //E-MAIL

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
        //PASSWORD

        if( password.isEmpty() || passwordConfField.getText().toString().isEmpty() ){
            warning+=getText(R.string.warning_passwEmpty).toString();
            isCorrect=false;
        }
        else {

            if( !password.equals( passwordConfField.getText().toString() ) ){
                warning += getText(R.string.warning_passwConfWrong).toString();
                isCorrect = false;
            }

            if (password.length() < 5) {
                warning += getText(R.string.warning_passwLengthmin).toString();
                isCorrect = false;
            }


        }

        //FINAL ACTION
        if( !isCorrect ){
            Toast.makeText(getApplicationContext(),warning,Toast.LENGTH_LONG).show();
        }

        return isCorrect;
    }


}
