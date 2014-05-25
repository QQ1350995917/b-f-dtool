package com.dingpw.tool.installer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class InstallerMainActivity extends Activity {
    private class OnLocationClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    private class OnInstallerClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            ApkInstaller.install(InstallerMainActivity.this,"iFly_TTS_ICS_4.0.apk");
        }
    }

    private TextView tv_location = null;
    private Button bt_installer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_installer_main);
        this.tv_location = (TextView)this.findViewById(R.id.tv_location);
        this.bt_installer = (Button)this.findViewById(R.id.bt_installer);
        this.tv_location.setOnClickListener(new OnLocationClickListener());
        this.bt_installer.setOnClickListener(new OnInstallerClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.installer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
