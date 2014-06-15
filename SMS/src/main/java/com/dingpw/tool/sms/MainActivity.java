package com.dingpw.tool.sms;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.dingpw.djson.DjsonArray;
import com.dingpw.djson.DjsonObject;

import java.io.File;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File(this.getString(R.string.string_export_dir));
        if(!file.exists()){
            file.mkdirs();
        }
        setContentView(new SMSView(this));
    }
}
