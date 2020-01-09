package com.sds.puzzledroid.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "puzzledroid.sqlite";
    private static final int DB_VERSION = 1;

    private static final String SCORE_TABLE = "CREATE TABLE IF NOT EXISTS scores (id INTEGER PRIMARY KEY AUTOINCREMENT, difficulty INTEGER, time_secs INTEGER, date_score TEXT);";

    public AdminOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }
}
