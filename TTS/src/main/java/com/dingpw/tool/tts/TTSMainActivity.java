package com.dingpw.tool.tts;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;


public class TTSMainActivity extends Activity implements TextToSpeech.OnInitListener {
    private Button speechButton;
    private EditText speechText;
    private TextToSpeech tts;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttsmain);
        tts = new TextToSpeech(this, this);

        speechText = (EditText) findViewById(R.id.speechTextEdit);
        speechButton = (Button) findViewById(R.id.speechButton);
        speechButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tts.speak(speechText.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {
            // 指定当前朗读的是英文，如果不是给予提示
            // int result = tts.setLanguage(Locale.US);
            // 指定当前语音引擎是中文，如果不是给予提示
            int result = tts.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(TTSMainActivity.this, R.string.string_notAvailable, Toast.LENGTH_LONG).show();
            }
        }
    }
}