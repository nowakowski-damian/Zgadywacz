package com.thirteendollars.guesser.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thirteendollars.guesser.R;
import com.thirteendollars.guesser.other.MediaManager;

public class LoginActivity extends AppCompatActivity {



    String nick;
    String password;
    EditText nickField;
    EditText passwField;
    CheckBox checkBox;

    Boolean loginSuccess;
    ViewSwitcher viewSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
  }

    @Override
    protected void onResume(){
        super.onResume();
        loginSuccess=false;
        viewSwitcher=(ViewSwitcher)findViewById(R.id.loginViewSwitcher);
        if(AppStaticData.rememberPassword){
            viewSwitcher.showNext();
            nick=AppStaticData.userName;
            password=AppStaticData.userPassword;
            new LogInParse().execute();
        }
        else {
            nickField = (EditText) findViewById(R.id.login_nick_editText);
            passwField = (EditText) findViewById((R.id.login_password_editText));
            checkBox = (CheckBox) findViewById(R.id.login_rememberme_checkbox);
        }
    }





    public void onSignup(View view) {

        startActivity(new Intent(this, SignupActivity.class));
    }


    public void onLogin(View view) {

        viewSwitcher.showNext();
        nick=nickField.getText().toString();
        password=passwField.getText().toString();
        if(checkBox.isChecked()){
            AppStaticData.userName=nick;
            AppStaticData.userPassword=password;
            AppStaticData.rememberPassword=true;
            AppStaticData.saveUserDataInSP();
        }
        new LogInParse().execute();


    }




    public void onForgotPassword(View view) {

        startActivity( new Intent(getApplicationContext(),ResetPassword.class) );
    }



    public class LogInParse extends AsyncTask<Void,Void,Void> {




        @Override
        protected Void doInBackground(Void... params) {




            ParseUser.logInInBackground(nick, password, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        // Hooray! The user is logged in.
                        loginSuccess=true;
                        ParseQuery<ParseUser> query = ParseUser.getQuery();

                        try {
                            if (query.get(ParseUser.getCurrentUser().getObjectId()).getBoolean("emailVerified")) {
                                if( !MediaManager.isInitialized() ) MediaManager.initialize(getApplicationContext()); // Initialize music service
                                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                               finish();

                            } else {
                                Toast.makeText(getApplicationContext(), R.string.email_not_confirmed, Toast.LENGTH_LONG).show();
                                loginSuccess=false;
                                viewSwitcher.showPrevious();
                            }


                        } catch (ParseException ex) {
                            Toast.makeText(getApplicationContext(),getErrorMessage(ex.getCode()), Toast.LENGTH_LONG).show();
                            loginSuccess=false;
                            viewSwitcher.showPrevious();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getErrorMessage(e.getCode()), Toast.LENGTH_LONG).show();
                        viewSwitcher.showPrevious();
                    }


                }
            });

            return null;
        }


    }


    private String getErrorMessage(int code){

        switch(code){
            case 101: return getString(R.string.invalid_login_parameters);
            case 100: return getString(R.string.connection_problems);
        }
        return  getString(R.string.unknown_error);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(!loginSuccess) AppStaticData.resetUserData();
    }
}
