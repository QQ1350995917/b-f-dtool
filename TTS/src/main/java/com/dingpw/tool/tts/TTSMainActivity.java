package com.dingpw.tool.tts;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class TTSMainActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TTSView(this));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}