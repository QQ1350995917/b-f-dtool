package com.dingpw.tool.tts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by dpw on 5/30/14.
 */
public class TTSStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getExtras().getString("data");
        System.out.println("================================= " + data);
    }
}
