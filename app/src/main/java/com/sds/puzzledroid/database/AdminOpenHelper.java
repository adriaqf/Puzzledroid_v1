package com.sds.puzzledroid.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AdminOpenHelper extends android.database.sqlite.SQLiteOpenHelper{

    private static final String DB_NAME = "puzzledroid.sqlite";
    private static final int DB_VERSION = 1;

    private static final String SCORE_TABLE = "CREATE TABLE score(id INTEGER PRIMARY KEY AUTOINCREMENT, timeSecs INTEGER, dateScore TEXT)";

    public AdminOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Executed when the user has another database version.
    }
}
