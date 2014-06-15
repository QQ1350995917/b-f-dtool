package com.dingpw.tool.ansiri;

import android.app.Activity;
import android.os.Bundle;

public class AnSiriActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new AnSiriView(this));
    }
}
