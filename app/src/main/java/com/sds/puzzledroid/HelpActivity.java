package com.sds.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends AppCompatActivity {

    private WebView wvHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        wvHelp = (WebView) findViewById(R.id.wv_help);
        wvHelp.getSettings().setJavaScriptEnabled(true);
        wvHelp.loadUrl("file:///android_asset/help.html");
    }

}
