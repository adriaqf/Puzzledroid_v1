package com.sds.puzzledroid.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sds.puzzledroid.pojos.Score;

import java.util.ArrayList;
import java.util.Locale;

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

    public void insertScore() {
        admin = new AdminOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        db.execSQL(String.format(Locale.getDefault(), "INSERT INTO Scores(difficulty, time_secs, date_score) VALUES(%d, %d, datetime('now', 'localtime'));",
                score.getDifficulty(), score.getTotalScore()));
        db.close();
    }

    public ArrayList<Score> getAllScores(int difficulty) {
        ArrayList<Score> scoreArrayList = new ArrayList<>();

        admin = new AdminOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();
        //Depending on the value of difficulty parameter, Cursor will has different values
        @SuppressLint("Recycle")
        Cursor cursor = difficulty == 0 ? db.rawQuery("select * from Scores where difficulty = 0 order by time_secs asc;", null) :
                difficulty == 1 ? db.rawQuery("select * from Scores where difficulty = 1 order by time_secs asc;", null) :
                        difficulty == 2 ? db.rawQuery("select * from Scores where difficulty = 2 order by time_secs asc;", null) :
                                db.rawQuery("select * from Scores order by time_secs asc;", null);

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