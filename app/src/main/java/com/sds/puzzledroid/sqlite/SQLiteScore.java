package com.sds.puzzledroid.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sds.puzzledroid.logic.Score;

import java.util.ArrayList;

public class SQLiteScore {

    private Context context;
    private Score score;
    private AdminOpenHelper admin;

    public SQLiteScore(Context context) {
        this.context = context;
    }

    public SQLiteScore(Context context, Score score) {
        this.context = context;
        this.score = score;
    }

    public boolean insertScore() {
        admin = new AdminOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        db.execSQL("INSERT INTO Scores(difficulty, time_secs, date_score) VALUES(" +
                score.getDifficulty() + ", " + score.getTotalScore() + ", " + "datetime()" + ");");

        db.close();
        return true;
    }

    public ArrayList<Score> getAllScores() {
        ArrayList<Score> scoreArrayList = new ArrayList<>();

        admin = new AdminOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Scores;", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Score pScore = new Score();
            pScore.setDifficulty(cursor.getInt(cursor.getColumnIndex("difficulty")));
            pScore.setTotalScore(cursor.getInt(cursor.getColumnIndex("time_secs")));
            pScore.setDateTime(cursor.getString(cursor.getColumnIndex("date_score")));
            scoreArrayList.add(pScore);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return scoreArrayList;
    }

}