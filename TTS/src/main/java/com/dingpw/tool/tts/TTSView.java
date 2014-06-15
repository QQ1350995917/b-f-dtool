package com.dingpw.tool.tts;

import android.app.Activity;
import android.content.*;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dingpw.tool.installer.ApkInstaller;

import java.util.Locale;

/**
 * Created by dingpw on 5/25/14.
 */
public class TTSView extends LinearLayout implements TextToSpeech.OnInitListener{

    private static final String DEFAULT_TTS_ENGINE = "com.iflytek.tts";
    private EditText editText = null;
    private Button button = null;
    private TextToSpeech textToSpeech = null;
    private int status = 0;


    public TTSView(Context context) {
        super(context);
        View.inflate(this.getContext(),R.layout.view_tts_error,this);
    }


    private void initTTS(){
        this.removeAllViews();
        try {
            this.textToSpeech = new TextToSpeech(this.getContext(), this);
        } catch (Exception e){

        }
        this.setOrientation(LinearLayout.VERTICAL);
        String defaultEngine = this.textToSpeech.getDefaultEngine();
        if(!ApkInstaller.isPackageNameInstalled(this.getContext(),DEFAULT_TTS_ENGINE)){
            View.inflate(this.getContext(),R.layout.view_tts_install,this);
            this.button = (Button)this.findViewById(R.id.bt_tts_install);
            this.button.setOnClickListener(new OnInstallButtonClickerListener());
        }else if(ApkInstaller.isPackageNameInstalled(this.getContext(),DEFAULT_TTS_ENGINE) && !DEFAULT_TTS_ENGINE.equals(defaultEngine)){
            Intent intent = new Intent();
            intent.setAction("com.android.settings.TTS_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.getContext().startActivity(intent);
        }else if(ApkInstaller.isPackageNameInstalled(this.getContext(),DEFAULT_TTS_ENGINE) && DEFAULT_TTS_ENGINE.equals(defaultEngine)){
            View.inflate(this.getContext(), R.layout.view_tts_edit, this);
            View.inflate(this.getContext(),R.layout.view_tts_reader,this);
            this.editText = (EditText)this.findViewById(R.id.et_tts_text);
            this.button = (Button)this.findViewById(R.id.bt_tts_reader);
            this.button.setOnClickListener(new OnReaderButtonClickerListener());
        }
    }

    @Override
    public void onInit(int status) {
        if (this.status == TextToSpeech.SUCCESS) {
        }
    }

    public void onResume(){
        this.initTTS();
    }

    private class OnReaderButtonClickerListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            textToSpeech.speak(editText.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private class OnInstallButtonClickerListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            ApkInstaller.installAssertsApkFile(TTSView.this.getContext(),"ifly_tts_ics.apk");
        }
    }
}
