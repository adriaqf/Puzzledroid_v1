package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import com.sds.puzzledroid.R;

public class HelpActivity extends AppCompatActivity {

    private WebView wvHelp;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //Modifying onClick button's event (toolbar_bottom)
        Toolbar toolbar = findViewById(R.id.toolbar_dynamic);
        backBtn = (ImageButton) toolbar.findViewById(R.id.btn_back_home);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wvHelp = (WebView) findViewById(R.id.wv_help);
        wvHelp.getSettings().setJavaScriptEnabled(true);
        wvHelp.loadUrl("file:///android_asset/help.html");
    }

}
