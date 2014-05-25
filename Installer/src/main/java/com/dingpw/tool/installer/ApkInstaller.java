package com.dingpw.tool.installer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by dingpw on 5/25/14.
 */
public class ApkInstaller {

    public static boolean isPackageNameInstalled(Context context,String packageName) {
        if (packageName == null || "".equals(packageName)){
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void install(Context context,String fileName){
        copyAssetsFile(context,fileName);
        File file = context.getFilesDir();
        System.out.println(file.getAbsolutePath() +"/"+ fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(file.getAbsolutePath() +"/"+ fileName)),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private static void copyAssetsFile(Context context,String fileName){
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = assetManager.open(fileName);
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }catch (Exception e){

        }finally {
            try{
                if(fileOutputStream != null){
                    fileOutputStream.flush();
                }
                if (inputStream != null){
                    inputStream.close();
                }
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            }catch (Exception e){

            }
        }
    }
}
