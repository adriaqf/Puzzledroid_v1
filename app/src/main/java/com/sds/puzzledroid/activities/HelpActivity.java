package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;

import com.sds.puzzledroid.R;

import java.util.Locale;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //Modifying onClick button's event (toolbar_bottom)
        Toolbar toolbar = findViewById(R.id.toolbar_dynamic);
        ImageButton backBtn = toolbar.findViewById(R.id.btn_back_home);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        WebView wvHelp = findViewById(R.id.wv_help);
        String L8= Locale.getDefault().toString();

        switch(L8)
        {
            case "en_US":
                wvHelp.loadUrl("file:///android_asset/helpEn.html");
                break;
            case "fr_FR":
                wvHelp.loadUrl("file:///android_asset/helpFr.html");
                break;
            default:
                wvHelp.loadUrl("file:///android_asset/help.html");
        }

    }


}
