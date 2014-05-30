package com.dingpw.tool.installer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dingpw.tool.filebrowser.FileBrowserView;

import java.io.File;
import java.io.FilenameFilter;


/**
 * Created by dingpw on 5/29/14.
 */
public class FileBrowserActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(new FileBrowserView(this).setRoot("/mnt/sdcard/").setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (new File(dir.getAbsolutePath() + System.getProperty("file.separator") + filename).isDirectory() && !filename.startsWith(".")) {
                    return true;
                }
                if (!new File(dir.getAbsolutePath() + System.getProperty("file.separator") + filename).isDirectory() && filename.endsWith(".apk")) {
                    return true;
                }
                return false;
            }
        }).setOnFileClickListener(new FileBrowserView.OnFileClickListener() {
            @Override
            public void onFileClick(File file) {
                Intent data = new Intent();
                data.putExtra(InstallerMainActivity.APK_PATH, file.getAbsolutePath());
                setResult(InstallerMainActivity.RETURN_CODE, data);
                FileBrowserActivity.this.finish();
            }
        }).initData());
    }
}
