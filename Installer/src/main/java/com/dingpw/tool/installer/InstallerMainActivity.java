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
import android.widget.Toast;
import com.sun.servicetag.Installer;


public class InstallerMainActivity extends Activity {
    private class OnLocationClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(InstallerMainActivity.this,FileBrowserActivity.class);
            InstallerMainActivity.this.startActivityForResult(intent,RETURN_CODE);
        }
    }

    private class OnInstallerClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //ApkInstaller.install(InstallerMainActivity.this,"iFly_TTS_ICS_4.0.apk");
            CharSequence text = tv_location.getText();
            if(text == null || text.toString().trim().equals("")){
                Toast.makeText(InstallerMainActivity.this,"文件路径为空",Toast.LENGTH_LONG).show();
            }else{
                ApkInstaller.install(InstallerMainActivity.this,text.toString());
            }
        }
    }

    private TextView tv_location = null;
    private Button bt_installer = null;
    public static final int RETURN_CODE = 1;
    public static final String APK_PATH = "apk_path";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RETURN_CODE){
            String path = data.getExtras().getString(APK_PATH);
            this.tv_location.setText(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
