package com.dingpw.tool.stt;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

import java.util.ArrayList;

/**
 * Created by dingpw on 6/1/14.
 */
public class STTView extends LinearLayout {
    private EditText et_sst_view_show = null;
    private RecognizerDialog rd = null;

    public STTView(Context context) {
        super(context);
        this.init();
    }

    private void init(){
        this.setOrientation(LinearLayout.VERTICAL);
        rd = new RecognizerDialog(this.getContext() ,"appid=50e1b967");
        View.inflate(this.getContext(),R.layout.view_stt_edit,this);
        View.inflate(this.getContext(),R.layout.view_stt_button,this);
        this.et_sst_view_show = (EditText)this.findViewById(R.id.et_sst_view_show);
        this.findViewById(R.id.bt_stt_view_speak).setOnClickListener(new OnSpeakClickListener());
    }

    private class OnSpeakClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            showReconigizerDialog();
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
        //设置识别后的回调结果
        rd.setListener(new RecognizerDialogListener() {
            @Override
            public void onResults(ArrayList<RecognizerResult> result, boolean isLast) {
                for (RecognizerResult recognizerResult : result) {
                    sb.append(recognizerResult.text);
                }
            }
            @Override
            public void onEnd(SpeechError error) {
                String string = sb.toString();
                et_sst_view_show.setText(string);
            }
        });

        et_sst_view_show.setText(""); //先设置为空，等识别完成后设置内容
        rd.show();
    }


}
