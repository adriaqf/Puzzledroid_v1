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

public class PopupCustomActivity extends AppCompatActivity {

    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_custom);

        Intent intent = getIntent();
        int totalScore = intent.getIntExtra("totalScore", 1);
        difficulty = intent.getIntExtra("difficulty", 1);

        TextView textView = findViewById(R.id.txt_result_popup);
        String txtResult = "Has tardado " + totalScore + " segundos";
        textView.setText(txtResult);


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SinglePlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }, 500);
    }

}
