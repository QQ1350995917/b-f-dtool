package com.dingpw.tool.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

/**
 * Created by dingpw on 6/1/14.
 */
public class TTSUtil implements TextToSpeech.OnInitListener{
    private static final String LOCK = "TTSUtil";
    private static TTSUtil ttsUtil = null;
    private TextToSpeech textToSpeech = null;

    private TTSUtil(Context context){
        this.textToSpeech = new TextToSpeech(context, this);
    }
    public static TTSUtil getInstance(Context context){
        if(ttsUtil == null){
            synchronized (LOCK){
                if(ttsUtil == null){
                    ttsUtil = new TTSUtil(context);
                }
            }
        }
        return ttsUtil;
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
        }
    }

    public void reader(String text){
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }
}
