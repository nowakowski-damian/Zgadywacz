package com.thirteendollars.guesser.wordslibrary;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper{

    public static final int[] LENGTH_START_ID=new int[]{0,0,0,1,251,1251,3251,6251,10251,15251,20251,24251,27251,29251,30751,31251,31501,31601,31651,31661,31666};
    public static final int[] LENGTH_COUNTER=new int[]{0,0,0,250,1000,2000,3000,4000,5000,5000,4000,3000,2000,1500,500,250,100,50,10,5,1};

    private  String DB_PATH;
    private final static String DB_NAME = "wordsDatabase";
    private final String TABLE_NAME="android_words";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    public DatabaseManager(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH="/data/data/"+context.getPackageName()+"/databases/";
    }




    public void open() throws SQLException{

        myDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();
        super.close();

    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Cursor getCursor(){
        return myDataBase.query(TABLE_NAME, null, null, null, null, null, null);

    }


    public AndroidWord getAndroidWord(int length) throws Exception{

        Cursor cursor= myDataBase.query(TABLE_NAME, new String[]{"_id", "word_length", "word", "guessed"}, "word_length=" + length, null, null, null, null);
        cursor.moveToFirst();

        int counter=0;
        while(cursor.getInt(3)==1){
            if(++counter>=LENGTH_COUNTER[length]) throw new Exception("No Words Available");
                else cursor.moveToNext();
        }
        return new AndroidWord(cursor.getInt(0),cursor.getInt(1),cursor.getString(2));

    }

    public boolean isWordInDB(String word){

        Cursor cursor= myDataBase.query(TABLE_NAME, new String[]{"_id", "word_length", "word", "guessed"}, "word=\""+word.toLowerCase()+"\"", null, null, null, null);
        if( cursor.moveToFirst() ) return true;
        else return false;
    }




    public void setWordGuessed(AndroidWord word){
        ContentValues newValues=new ContentValues();
        newValues.put("guessed",Integer.valueOf(1));
        myDataBase.update(TABLE_NAME,newValues,"_id="+word.getId(),null);
    }


}