package com.dingpw.tool.tts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.dingpw.tool.installer.ApkInstaller;

import java.util.Locale;

/**
 * Created by dingpw on 5/25/14.
 */
public class TTSView extends LinearLayout implements TextToSpeech.OnInitListener{

    private EditText editText = null;
    private Button button = null;
    private TextToSpeech textToSpeech = null;

    public TTSView(Context context) {
        super(context);
        this.textToSpeech = new TextToSpeech(context, this);
    }

    public TTSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.textToSpeech = new TextToSpeech(context, this);

        this.getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println(" ============================================== " + intent.getAction());
            }
        },null);
    }

    private void initView(Context context,boolean runing){
        this.setOrientation(LinearLayout.VERTICAL);
        if(runing){
            View.inflate(this.getContext(),R.layout.view_tts_edit,this);
            View.inflate(this.getContext(),R.layout.view_tts_reader,this);
            this.editText = (EditText)this.findViewById(R.id.et_tts_text);
            this.button = (Button)this.findViewById(R.id.bt_tts_reader);
            this.button.setOnClickListener(new OnReaderButtonClickerListener());
        }else{
            View.inflate(this.getContext(),R.layout.view_tts_install,this);
            this.button = (Button)this.findViewById(R.id.bt_tts_install);
            this.setOnClickListener(new OnInstallButtonClickerListener());
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 指定当前朗读的是英文，如果不是给予提示
            // int result = tts.setLanguage(Locale.US);
            // 指定当前语音引擎是中文，如果不是给予提示
            int result = this.textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //Toast.makeText(this.getContext(), R.string.string_notAvailable, Toast.LENGTH_LONG).show();
                if(ApkInstaller.isPackageNameInstalled(this.getContext(),"com.iflytek.tts")){
                    System.out.println("================================请设置");
                }else{
                    System.out.println("================================请安装");
                    this.initView(this.getContext(),false);
                }
            }else{
                this.initView(this.getContext(),true);
            }
        }
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
