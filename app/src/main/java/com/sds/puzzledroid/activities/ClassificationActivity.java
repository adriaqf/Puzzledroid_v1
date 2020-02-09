package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.adapters.ItemClassificationLVAdapter;
import com.sds.puzzledroid.logic.Score;
import com.sds.puzzledroid.sqlite.SQLiteScore;

import java.util.ArrayList;

public class ClassificationActivity extends AppCompatActivity {

    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        Intent i = getIntent();
        difficulty = i.getIntExtra("difficulty", 1);

        FrameLayout frameLayout = findViewById(R.id.fl_header_classification);
        ViewCompat.setTranslationZ(frameLayout, 1);

        ListView listView = findViewById(R.id.lv_classification);
        ItemClassificationLVAdapter itemLVAdapter = new ItemClassificationLVAdapter(this, getScores());
        listView.setAdapter(itemLVAdapter);

        //Modifying onClick button's event (toolbar_bottom)
        Toolbar toolbar = findViewById(R.id.toolbar_dynamic);
        ImageButton backBtn = toolbar.findViewById(R.id.btn_back_home);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public ArrayList<Score> getScores() {
        ArrayList<Score> scores;
        SQLiteScore sqLiteScore = new SQLiteScore(this);

        scores = sqLiteScore.getAllScores(difficulty);

        return scores;
    }

}
