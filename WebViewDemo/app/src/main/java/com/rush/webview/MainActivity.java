package com.rush.webview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rush.com.base.PluginInit;
import rush.com.webkit.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PluginInit.init();
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }
}
