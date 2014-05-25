package com.dingpw.tool.filebrowser;

import android.content.Context;

import java.io.File;

/**
 * Created by dingpw on 5/25/14.
 */
public class FileBrowser {

    public static void browser(Context context,String parent){
        File parentFile = new File("parent");
        File[] subFiles = parentFile.listFiles();
        int len = subFiles.length;
        com.dingpw.djson.Djson.createJsonObject();
        for(int i=0;i<len;i++){
            File subFile = subFiles[i];
            if(subFile.isDirectory()){

            }
            String name = subFile.getName();
        }
    }
}
