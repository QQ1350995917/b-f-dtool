package com.dingpw.tool.installer;

import android.app.Activity;
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
        this.setContentView(new FileBrowserView(this).setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if(dir.isDirectory() && !filename.startsWith(".")){
                    return true;
                }else if(!dir.isDirectory() && filename.endsWith(".apk")){
                    return true;
                }
                return false;
            }
        }).initData());
    }
}
