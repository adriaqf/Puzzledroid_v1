package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.adapters.ItemLVAdapter;
import com.sds.puzzledroid.logic.Score;
import com.sds.puzzledroid.sqlite.AdminOpenHelper;
import com.sds.puzzledroid.sqlite.SQLiteScore;

import java.util.ArrayList;

public class ClassificationActivity extends AppCompatActivity {

    private ListView listView;
    private ItemLVAdapter itemLVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        listView = findViewById(R.id.lv_classification);
        itemLVAdapter = new ItemLVAdapter(this, getScore());
        listView.setAdapter(itemLVAdapter);
    }

    private ArrayList<Score> getScore() {
        ArrayList<Score> scores;
        SQLiteScore sqLiteScore = new SQLiteScore(this);

        scores = sqLiteScore.getAllScores();

        return scores;
    }
}
