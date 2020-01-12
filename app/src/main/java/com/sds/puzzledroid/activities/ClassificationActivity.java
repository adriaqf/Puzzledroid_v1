package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.adapters.ItemLVAdapter;
import com.sds.puzzledroid.logic.Score;
import com.sds.puzzledroid.sqlite.AdminOpenHelper;
import com.sds.puzzledroid.sqlite.SQLiteScore;

import java.util.ArrayList;

public class ClassificationActivity extends AppCompatActivity {

    private int difficulty;
    private FrameLayout frameLayout;
    private ListView listView;
    private ItemLVAdapter itemLVAdapter;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        Intent i = getIntent();
        difficulty = i.getIntExtra("difficulty", 1);

        frameLayout = findViewById(R.id.fl_header_classification);
        ViewCompat.setTranslationZ(frameLayout, 1);

        listView = findViewById(R.id.lv_classification);
        itemLVAdapter = new ItemLVAdapter(this, getScores());
        listView.setAdapter(itemLVAdapter);

        //Modifying onClick button's event (toolbar_bottom)
        Toolbar toolbar = findViewById(R.id.toolbar_dynamic);
        backBtn = toolbar.findViewById(R.id.btn_back_home);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ArrayList<Score> getScores() {
        ArrayList<Score> scores;
        SQLiteScore sqLiteScore = new SQLiteScore(this);

        scores = sqLiteScore.getAllScores(difficulty);

        return scores;
    }

    /*private void onClickRefreshLV(View view) {
        switch (view.getId()) {
            case R.id.btn_classif0:
                listView = findViewById(R.id.lv_classification);
                itemLVAdapter = new ItemLVAdapter(this, getScores(0));
                listView.setAdapter(itemLVAdapter);
                break;
            case R.id.btn_classif1:
                listView = findViewById(R.id.lv_classification);
                itemLVAdapter = new ItemLVAdapter(this, getScores(1));
                listView.setAdapter(itemLVAdapter);
                break;
            case R.id.btn_classif2:
                listView = findViewById(R.id.lv_classification);
                itemLVAdapter = new ItemLVAdapter(this, getScores(2));
                listView.setAdapter(itemLVAdapter);
                break;
        }
    }*/
}
