package com.dingpw.tool.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        this.init(context);
    }

    public TTSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context){
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.textToSpeech = new TextToSpeech(context, this);

        this.editText = new EditText(context);
        LayoutParams editTextLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,4);
        this.editText.setLayoutParams(editTextLayoutParams);
        this.editText.setHint(R.string.string_demo);

        this.button = new Button(context);
        LayoutParams buttonLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
        this.button.setLayoutParams(buttonLayoutParams);
        this.button.setText(R.string.string_reader);
        this.button.setOnClickListener(new OnReaderButtonClickerListener());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 指定当前朗读的是英文，如果不是给予提示
            // int result = tts.setLanguage(Locale.US);
            // 指定当前语音引擎是中文，如果不是给予提示
            int result = this.textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this.getContext(), R.string.string_notAvailable, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class OnReaderButtonClickerListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            textToSpeech.speak(editText.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
