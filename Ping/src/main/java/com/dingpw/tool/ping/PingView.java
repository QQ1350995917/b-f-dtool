package com.dingpw.tool.ping;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.*;
import com.dingpw.djson.Djson;
import com.dingpw.djson.DjsonArray;
import com.dingpw.djson.DjsonObject;

/**
 * Created by dingpw on 6/15/14.
 */
public class PingView extends LinearLayout implements IPingCallBack{
    private EditText editText = null;
    private Button button = null;
    private ProgressBar progressBar = null;
    private TextView textView = null;

    public PingView(Context context) {
        super(context);
        this.init();
    }
    
    private void init(){
        this.setOrientation(LinearLayout.VERTICAL);
        editText = new EditText(this.getContext());
        editText.setText("192.168.");
        CharSequence text = editText.getText();
        if (text instanceof Spannable) {
           Spannable spanText = (Spannable)text;
           Selection.setSelection(spanText, text.length());
        }
        editText.setTextColor(Color.WHITE);
        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        editText.setBackgroundResource(R.drawable.btn_lock_normal);
        button = new Button(this.getContext());
        button.setText("Ping");
        button.setTextColor(Color.WHITE);
        button.setBackgroundResource(R.drawable.btn_lock_normal);
        button.setOnClickListener(new OnPingClickListener());
        progressBar = new ProgressBar(this.getContext());
        progressBar.setVisibility(View.GONE);
        ScrollView scrollView = new ScrollView(this.getContext());
        textView = new TextView(this.getContext());
        textView.setTextColor(Color.WHITE);
        scrollView.addView(textView);
        this.addView(editText);
        this.addView(button);
        this.addView(progressBar);
        this.addView(scrollView);

    }

    @Override
    public void onStart(DjsonObject djsonObject) {
        this.editText.setEnabled(false);
        this.button.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressing(DjsonObject djsonObject) {
        String text = "";
        DjsonArray message = djsonObject.getDjsonArray("message");
        for(int i=0;i<message.getLenght();i++){
            text = text + message.getString(i) + "\r\n";
        }
        this.textView.setText(text);
    }

    @Override
    public void onEnd(DjsonObject djsonObject) {
        this.editText.setEnabled(true);
        this.button.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.GONE);
    }

    private class OnPingClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            textView.setText("");
            new PingTask(PingView.this).execute(Djson.createJsonObject().set("ip",editText.getText().toString()));
        }
    }

}
