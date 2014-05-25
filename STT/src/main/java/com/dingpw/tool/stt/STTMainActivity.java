package com.dingpw.tool.stt;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

import java.util.ArrayList;


public class STTMainActivity extends Activity implements View.OnClickListener {
    protected static final String TAG = "STTMainActivity";
    private EditText txt_result;
    private RecognizerDialog rd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sttmain);

        findView();
        //RecognizerDialog(Context context, String params); "appid=1234567,usr=test,pwd=12345"  usr、pwd不是必选的
        //创建语音识别dailog对象，appid到讯飞就注册获取
        rd = new RecognizerDialog(this ,"appid=50e1b967");
    }

    private void findView() {
        txt_result = (EditText) findViewById(R.id.txt_result);
        findViewById(R.id.bt_search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:
                showReconigizerDialog();
                break;

            default:
                break;
        }
    }

    private void showReconigizerDialog() {
        //setEngine(String engine,String params,String grammar);
        /**
         * 识别引擎选择，目前支持以下五种
         “sms”：普通文本转写
         “poi”：地名搜索
         “vsearch”：热词搜索
         “vsearch”：热词搜索
         “video”：视频音乐搜索
         “asr”：命令词识别

         params	引擎参数配置列表
         附加参数列表，每项中间以逗号分隔，如在地图搜索时可指定搜索区域：“area=安徽省合肥市”，无附加参数传null
         */
        rd.setEngine("sms", null, null);

        //设置采样频率，默认是16k，android手机一般只支持8k、16k.为了更好的识别，直接弄成16k即可。
        rd.setSampleRate(com.iflytek.speech.SpeechConfig.RATE.rate16k);

        final StringBuilder sb = new StringBuilder();
        Log.i(TAG, "识别准备开始.............");

        //设置识别后的回调结果
        rd.setListener(new RecognizerDialogListener() {
            @Override
            public void onResults(ArrayList<RecognizerResult> result, boolean isLast) {
                for (RecognizerResult recognizerResult : result) {
                    sb.append(recognizerResult.text);
                    Log.i(TAG, "识别一条结果为::" + recognizerResult.text);
                }
            }
            @Override
            public void onEnd(SpeechError error) {
                Log.i(TAG, "识别完成.............");
                txt_result.setText(sb.toString());
                Log.i(TAG, "识别完成:"+txt_result.getText().toString());
            }
        });

        txt_result.setText(""); //先设置为空，等识别完成后设置内容
        rd.show();
    }

}