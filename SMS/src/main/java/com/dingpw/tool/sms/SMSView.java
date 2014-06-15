package com.dingpw.tool.sms;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.dingpw.djson.Djson;
import com.dingpw.djson.DjsonObject;

/**
 * Created by dingpw on 6/14/14.
 */
public class SMSView extends LinearLayout implements ISmsCallback,SMSConstant{

    private EditText ed_export = null;
    private Button bt_export = null;
    private Button bt_export_open_file = null;
    private Button bt_import_location = null;
    private Button bt_import = null;
    private ProgressBar pb_progress = null;

    public SMSView(Context context) {
        super(context);
        this.init();
    }

    private void init(){
        View.inflate(this.getContext(), R.layout.view_main, this);
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("export").setIndicator(this.getContext().getString(R.string.string_export_sms),getResources().getDrawable(R.drawable.ic_launcher)).setContent(R.id.ll_export));
        tabHost.addTab(tabHost.newTabSpec("import").setIndicator(this.getContext().getString(R.string.string_import_sms),getResources().getDrawable(R.drawable.ic_launcher)).setContent(R.id.ll_import));

        this.ed_export = (EditText) this.findViewById(R.id.ed_export);
        this.bt_export = (Button) this.findViewById(R.id.bt_export);
        this.bt_export_open_file = (Button) this.findViewById(R.id.bt_export_open_file);
        this.bt_import_location = (Button) this.findViewById(R.id.bt_import_location);
        this.bt_import = (Button) this.findViewById(R.id.bt_import);
        this.pb_progress = (ProgressBar) this.findViewById(R.id.pb_progress);

        //this.ed_export.addTextChangedListener(new OnPhoneTextChageListener());
        this.bt_export.setOnClickListener(new OnExportClickListener());
        this.bt_export_open_file.setOnClickListener(new OnOpenFileDirClickListener());
    }

    @Override
    public void onStart(DjsonObject djsonObject) {
        this.pb_progress.setVisibility(View.VISIBLE);
        this.bt_export_open_file.setVisibility(View.VISIBLE);
        this.bt_export_open_file.setText("正在准备...");
    }

    @Override
    public void onProgressing(DjsonObject djsonObject) {
        if("prepared".equals(djsonObject.getString(KEY_STATUS))){
            this.bt_export_open_file.setText("准备导出" + djsonObject.getString(KEY_TOTAL) + "条数据");
        }else if("progressing".equals(djsonObject.getString(KEY_STATUS))){
            this.bt_export_open_file.setText("正在导出" + djsonObject.getString(KEY_INDEX) + "/" + djsonObject.getString(KEY_TOTAL) + (djsonObject.getBoolean(KEY_SERIALIZATION) ? "成功" : "失败"));
        }else if("error".equals(djsonObject.getString(KEY_STATUS))){
            this.bt_export_open_file.setText("导出失败" + djsonObject.getString(KEY_ERROR_MESSAGE));
        }
    }

    @Override
    public void onEnd(DjsonObject djsonObject) {
        this.pb_progress.setVisibility(View.GONE);
        this.bt_export_open_file.setVisibility(View.VISIBLE);
        if(djsonObject.getNumber(KEY_TOTAL).intValue() == 0){
            this.bt_export_open_file.setText("导出数据是0行");
            this.bt_export_open_file.setClickable(false);
        }else{
            this.bt_export_open_file.setClickable(true);
            this.bt_export_open_file.setText(R.string.string_export_dir);
            Toast.makeText(this.getContext(),R.string.string_export_tip_end,Toast.LENGTH_LONG).show();
        }
    }

    private class OnOpenFileDirClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(SMSView.this.getContext(),"这个按钮将会打开该文件夹",Toast.LENGTH_LONG).show();
        }
    }

    private class OnExportClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            int length = ed_export.getText().toString().length();
            if(length > 0 && length < 13){
                Toast.makeText(SMSView.this.getContext(),R.string.string_import_tip_error_phone_number,Toast.LENGTH_LONG).show();
                return;
            }
            new SMSExportTask(SMSView.this.getContext(),SMSView.this).execute(Djson.createJsonObject().set(KEY_PHONE_NUMBER,ed_export.getText().toString()));
        }
    }

    private class OnPhoneTextChageListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String contents = s.toString();
            int length = contents.length();
            if(length == 4){
                if(contents.substring(3).equals(new String(" "))){
                    contents = contents.substring(0, 3);
                    ed_export.setText(contents);
                    ed_export.setSelection(contents.length());
                }else{
                    contents = contents.substring(0, 3) + " " + contents.substring(3);
                    ed_export.setText(contents);
                    ed_export.setSelection(contents.length());
                }
            } else if(length == 9){
                if(contents.substring(8).equals(new String(" "))){
                    contents = contents.substring(0, 8);
                    ed_export.setText(contents);
                    ed_export.setSelection(contents.length());
                }else{
                    contents = contents.substring(0, 8) + " " + contents.substring(8);
                    ed_export.setText(contents);
                    ed_export.setSelection(contents.length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
