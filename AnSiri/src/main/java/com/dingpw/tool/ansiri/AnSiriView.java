package com.dingpw.tool.ansiri;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dingpw.tool.stt.STTSpeakerView;
import com.dingpw.tool.tts.TTSUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by dingpw on 6/1/14.
 */
public class AnSiriView extends RelativeLayout{

    private static final String URL = "http://www.tuling123.com/openapi/api?";
    private static final String KEY = "7b5800a98ae71d09edd0c66436e404e2";


    private TextView tv_ansiri_item_left = null;
    private TextView tv_ansiri_item_right = null;

    private TTSUtil ttsUtil = null;


    public AnSiriView(Context context) {
        super(context);
        this.init();
    }

    private void init(){
        this.ttsUtil = TTSUtil.getInstance(AnSiriView.this.getContext());
        View.inflate(this.getContext(),R.layout.view_ansiri_item_left,this);
        View.inflate(this.getContext(),R.layout.view_ansiri_item_right,this);
        this.tv_ansiri_item_left = (TextView)this.findViewById(R.id.tv_ansiri_item_left);
        this.tv_ansiri_item_right = (TextView) this.findViewById(R.id.tv_ansiri_item_right);
        STTSpeakerView speakerView = new STTSpeakerView(this.getContext());
        speakerView.setBackgroundResource(R.drawable.btn_lock_normal);
        speakerView.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        speakerView.setLayoutParams(layoutParams);
        this.addView(speakerView.setOnSpeakEndCallBack(new OnMySpeakEndCallBack()));
    }

    private class OnMySpeakEndCallBack implements STTSpeakerView.SpeakEndCallBack{
        @Override
        public void callBack(String text) {
            tv_ansiri_item_left.setText(text);
            get(handler,text);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String text = msg.obj.toString();
            tv_ansiri_item_right.setText(text);
            ttsUtil.reader(text);
        }
    };

    private void get(final Handler handelr ,final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URL + "key=" + KEY +"&info="+text;
                System.out.println(url);
                HttpGet get = new HttpGet(url);
                HttpClient client = new DefaultHttpClient();
                Message msg = new Message();
                try {
                    HttpResponse response = client.execute(get);
                    String resultString = EntityUtils.toString(response.getEntity());
                    msg.obj = resultString;
                } catch (Exception e) {
                    msg.obj = "你的网络不稳定，我不想回答你的问题";
                }
                handelr.sendMessage(msg);
            }
        }).start();
    }
}
