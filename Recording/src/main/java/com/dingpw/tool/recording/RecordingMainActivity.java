package com.dingpw.tool.recording;

import android.app.Activity;
import android.os.Bundle;


public class RecordingMainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(new RecordingView(this));
    }
}
