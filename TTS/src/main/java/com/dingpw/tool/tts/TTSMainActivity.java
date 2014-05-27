package com.dingpw.tool.tts;

import android.app.Activity;
import android.os.Bundle;

public class TTSMainActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TTSView(this));
    }
}