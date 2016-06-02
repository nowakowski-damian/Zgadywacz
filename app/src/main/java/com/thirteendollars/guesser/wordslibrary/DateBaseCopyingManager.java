package com.thirteendollars.guesser.wordslibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DateBaseCopyingManager extends SQLiteOpenHelper {


    private  String DB_PATH;
    private final static String DB_NAME = "wordsDatabase";
    private final Context myContext;


    public DateBaseCopyingManager (Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH="/data/data/"+context.getPackageName()+"/databases/";
        this.getReadableDatabase(); //create empty database in default system directory
        try { copyDataBase(); }  //copy database from assets to just created empty database
        catch(IOException e){ Toast.makeText(myContext,e.getMessage(), Toast.LENGTH_LONG).show();};
    }






    public void copyDataBase() throws IOException{

        InputStream input = myContext.getAssets().open(DB_NAME);
        OutputStream output = new FileOutputStream(DB_PATH + DB_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ( (length = input.read(buffer))>0 ){
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();

    }

    public boolean isDBexist(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){ /* database probably doesn't exist */ }

        if(checkDB == null) return false;
        //else
        checkDB.close();
        return true;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
