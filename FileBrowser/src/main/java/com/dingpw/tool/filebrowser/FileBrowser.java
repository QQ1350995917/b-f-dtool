package com.dingpw.tool.filebrowser;

import android.content.Context;
import com.dingpw.djson.Djson;
import com.dingpw.djson.DjsonArray;
import com.dingpw.djson.DjsonObject;

import java.io.File;

/**
 * Created by dingpw on 5/25/14.
 */
public class FileBrowser {

    public static DjsonArray browser(String parent){
        File parentFile = new File(parent);
        File[] subFiles = parentFile.listFiles();
        int len = subFiles.length;
        DjsonArray list = Djson.createJsonArray();
        for(int i=0;i<len;i++){
           DjsonObject item = Djson.createJsonObject();
            File subFile = subFiles[i];
            if(subFile.isDirectory()){
                item.set(FileBrowserConstant.KEY_PROPERTY,FileBrowserConstant.VALUE_DIR);
            }
            String name = subFile.getName();
            item.set(FileBrowserConstant.KEY_NAME,name);
            String path = subFile.getAbsolutePath();
            item.set(FileBrowserConstant.KEY_PATH,path);
            list.push(item);
        }
        return list;
    }
}
