package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sds.puzzledroid.R;

import java.util.Locale;

public class PopupCustomActivity extends AppCompatActivity {

    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_custom);

        Intent intent = getIntent();
        int totalScore = intent.getIntExtra("totalScore", 1);
        difficulty = intent.getIntExtra("difficulty", 1);

        String L8 = Locale.getDefault().toString();

        switch(L8)
        {
            case "en_US":
                TextView textView = findViewById(R.id.txt_result_popup);
                String txtResult = "it took you " + totalScore + " seconds";
                textView.setText(txtResult);
                break;
            case "fr_FR":
                TextView textView2 = findViewById(R.id.txt_result_popup);
                String txtResult2 = "il vous a fallu " + totalScore + " secondes";
                textView2.setText(txtResult2);
                break;
            default:
                TextView textView3 = findViewById(R.id.txt_result_popup);
                String txtResult3 = "Has tardado " + totalScore + " segundos";
                textView3.setText(txtResult3);
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int heigh = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(heigh*0.7));

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 100;

        getWindow().setAttributes(layoutParams);
    }

    public void onClickGoTo(View view) {
        switch (view.getId()) {
            case R.id.btn_close_popup:
                finish();
                break;
            case R.id.btn_classification_popup:
                Intent i = new Intent(this, ClassificationActivity.class);
                i.putExtra("difficulty", difficulty);
                startActivity(i);
                break;
        }
    }

}
