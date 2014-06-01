package com.dingpw.tool.stt;

import android.app.Activity;
import android.os.Bundle;


public class STTMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new STTView(this));
    }
}