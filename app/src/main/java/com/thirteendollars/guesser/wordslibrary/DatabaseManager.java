package com.thirteendollars.guesser.wordslibrary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class DatabaseManager extends SQLiteOpenHelper {

    @SuppressLint("StaticFieldLeak") // it's application context
    private static DatabaseManager INSTANCE;
    private static final int[] LENGTH_COUNTER=new int[]{0,0,0,250,1000,2000,3000,4000,5000,5000,4000,3000,2000,1500,500,250,100,50,10,5,1};
    private static final String DB_CREATE_SCRPIT_NAME = "android_words_create_db.sql";
    private final static String DB_NAME = "wordsDatabase";
    private final String TABLE_NAME="android_words";
    private Context context;
    private DatabaseCallback callback;

    public static DatabaseManager getInstance(Context context) {
        if(INSTANCE==null) {
            INSTANCE = new DatabaseManager(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public void initialize(DatabaseCallback callback) {
        this.callback = callback;
        if( isDbExists(context, DB_NAME) ) {
            callback.onDatabaseAlreadyExists();
        } else {
            callback.onDatabaseInitializationStarted();
            getReadableDatabase(); // force initialization
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        PopulateDatabase populateDatabaseTask = new PopulateDatabase(
                context,
                DB_CREATE_SCRPIT_NAME,
                db,
                callback
        );
        populateDatabaseTask.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static boolean isDbExists(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public AndroidWord getAndroidWord(int length) throws Exception{
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(
                TABLE_NAME,
                new String[]{"_id", "word_length", "word", "guessed"},
                "word_length=" + length,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        int counter=0;
        while(cursor.getInt(3)==1){
            if(++counter>=LENGTH_COUNTER[length]) throw new Exception("No Words Available");
            else cursor.moveToNext();
        }
        AndroidWord word =  new AndroidWord(cursor.getInt(0),cursor.getInt(1),cursor.getString(2));
        cursor.close();
        db.close();
        return word;
    }

    public void setWordGuessed(AndroidWord word){
        ContentValues newValues = new ContentValues();
        newValues.put("guessed",1);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME,newValues,"_id="+word.getId(),null);
        db.close();
    }

    private static class PopulateDatabase extends AsyncTask<Void,Void,Boolean> {

        private Context context;
        private String assetsPath;
        private SQLiteDatabase db;
        private DatabaseCallback callback;

        public PopulateDatabase(Context context, String assetsPath, SQLiteDatabase db, DatabaseCallback callback) {
            this.context = context;
            this.assetsPath = assetsPath;
            this.db = db;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String scriptSql = loadTextFromAssets(context, assetsPath);
                for (String query : scriptSql.split(";")) {
                    Log.d(getClass().getCanonicalName(),"Exec: "+query);
                    db.execSQL(query);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if(callback==null) return;
            if(success) {
                callback.onDatabasePopulatedSuccess();
            } else {
                callback.onDatabasePopulatedError();
            }
        }

        private String loadTextFromAssets(Context context, String assetsPath) throws IOException {
            InputStream is = context.getResources().getAssets().open(assetsPath);
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int length = is.read(buffer); length != -1; length = is.read(buffer)) {
                baos.write(buffer, 0, length);
            }
            is.close();
            baos.close();
            return new String(baos.toByteArray(), "UTF-8").replaceAll("[\n\t]","");
        }
    }

    public interface DatabaseCallback {
        void onDatabaseInitializationStarted();
        void onDatabaseAlreadyExists();
        void onDatabasePopulatedSuccess();
        void onDatabasePopulatedError();
    }
}
