package com.sds.puzzledroid.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.sds.puzzledroid.logic.Score;

import java.util.ArrayList;

public class SQLiteScore {

    private Context context;
    private Score score;
    private AdminOpenHelper admin;

    public SQLiteScore(Context context, Score score) {
        this.context = context;
        this.score = score;
    }

    public boolean insertScore(Score score) {
        admin = new AdminOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("difficulty", score.getDifficulty());
        contentValues.put("timeSecs", score.getTotalScore());
        contentValues.put("dateScore", score.getDateTime());

        db.insert("score", null, contentValues);

        return true;
    }

    public ArrayList<Score> getAllScores() {
        ArrayList<Score> scoreArrayList = new ArrayList<>();
        Score score = new Score();

        admin = new AdminOpenHelper(context);
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from score", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            score.setDifficulty(cursor.getInt(cursor.getColumnIndex("difficulty")));
            score.setTotalScore(cursor.getInt(cursor.getColumnIndex("timeSecs")));
            score.setDateTime(cursor.getString(cursor.getColumnIndex("dateScore")));
            scoreArrayList.add(score);

            cursor.moveToNext();
        }

        return scoreArrayList;
    }

}