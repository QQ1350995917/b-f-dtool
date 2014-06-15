package com.dingpw.tool.tts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

public class TTSMainActivity extends Activity{
    private IflyTTSStatusReceiver receiver = null;
    private TTSView ttsView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.ttsView = new TTSView(this));
        //this.receiver = new IflyTTSStatusReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(this.receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        //this.registerReceiver(this.receiver, intentFilter);
        this.ttsView.onResume();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    private class IflyTTSStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean packageAdd = intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED);
            System.out.println("监听到新的安装包已经执行安装");
            if(packageAdd){
                if(intent.getDataString().endsWith("com.iflytek.tts")){
                    System.out.println("监听到com.iflytek.tts已经安装");
                }
            }
        }
    }
}