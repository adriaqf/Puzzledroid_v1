package com.sds.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class ClassificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
    }

    private void getScore() {
        AdminOpenHelper admin = new AdminOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        //SELECT USING CURSOR CLASS + INSERT ALL VALUES IN AN ARRAY

    }
}
