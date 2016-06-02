package com.thirteendollars.guesser.wordslibrary;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thirteendollars.guesser.app.AppStaticData;

import java.util.List;

/**
 * Created by Damian on 2016-01-09.
 */
public class UserWord{

    private String id;
    private int length;
    private String word;
    private String owner;

    public UserWord(ParseObject word) {
        this.id = word.getObjectId();
        this.length = word.getInt("length");
        this.word = word.getString("word");
        this.owner = word.getString("ownerId");
    }

    public String getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public String getWord() {
        return word.toUpperCase();
    }

    public String getOwner() {
        return owner;
    }

    public  void setWordGuessed(){
        AppStaticData.spEditor.putBoolean(id,true);
        AppStaticData.spEditor.commit();
    }

    public boolean isGuessed(){
        return AppStaticData.sp.getBoolean(id,false);
    }


    public static boolean isGuessed(ParseObject word){
        return AppStaticData.sp.getBoolean( word.getObjectId(),false );
    }


    public  void sendCashFromLoss() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", owner);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    try {
                        objects.get(0).increment("incomes");
                        objects.get(0).saveInBackground();
                    }
                    catch( IndexOutOfBoundsException exception){}


                }
            }
        });

    }


    public void incrementCasualtiesNum(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserWords");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject uword, ParseException e) {
                if (e == null) { uword.increment("casualties"); uword.saveInBackground(); }
            }
        });
    }



}

