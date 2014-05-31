package com.dingpw.tool.recording;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dingpw on 5/31/14.
 */
public class RecordingView extends RelativeLayout {

    private boolean sdCardExit;
    private boolean isStopRecord;
    private ListView lv_list = null;
    private ListView myListView1;
    private File myRecAudioDir = null;
    private ArrayList<String> recordFiles = null;
    private ArrayAdapter<String> adapter = null;
    private Button bt_start = null;
    private Button bt_finish = null;
    private Button bt_play = null;
    private Button bt_delete = null;
    private String strTempFile = "dtoolRecording";
    private File myRecAudioFile = null;
    private MediaRecorder mMediaRecorder01 = null;
    private File myPlayFile = null;
    private Timer timer = new Timer();
    private TextView tv_timer = null;
    private int counter = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_timer.setText(counter + "s");
        }
    };

    public RecordingView(Context context) {
        super(context);
        this.init();
    }

    private void init(){
        View.inflate(this.getContext(),R.layout.view_recording_list,this);
        View.inflate(this.getContext(), R.layout.view_recording_button, this);
        View.inflate(this.getContext(),R.layout.view_recording_timer,this);
        this.tv_timer = (TextView)this.findViewById(R.id.tv_timer);
        this.myListView1 = (ListView) findViewById(R.id.lv_list);
        this.bt_start = (Button) this.findViewById(R.id.bt_start);
        this.bt_start.setOnClickListener(new OnStartButtonClickListener());
        this.bt_finish = (Button)this.findViewById(R.id.bt_finish);
        this.bt_finish.setOnClickListener(new OnFinishButtonClickListener());
        this.bt_play = (Button)this.findViewById(R.id.bt_play);
        this.bt_play.setOnClickListener(new OnPlayButtonClickListener());
        this.bt_delete = (Button)this.findViewById(R.id.bt_delete);
        this.bt_delete.setOnClickListener(new OnDeleteButtonClickListener());

        sdCardExit = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExit){
            myRecAudioDir = Environment.getExternalStorageDirectory();
        }
        getRecordFiles();
        adapter = new ArrayAdapter<String>(this.getContext(), R.layout.view_recording_list_item, recordFiles);
        myListView1.setAdapter(adapter);
        myListView1.setOnItemClickListener(new OnListViewItemClickListener());
    }



    private void getRecordFiles(){
        recordFiles = new ArrayList<String>();
        if (sdCardExit){
            File files[] = myRecAudioDir.listFiles();
            if (files != null){
                for (int i = 0; i < files.length; i++){
                    if (files[i].getName().indexOf(".") >= 0){
                        /* 读取.amr文件 */
                        String fileS = files[i].getName().substring(files[i].getName().indexOf("."));
                        if (fileS.toLowerCase().equals(".amr")){
                            recordFiles.add(files[i].getName());
                        }
                    }
                }
            }
        }
    }

    private class OnStartButtonClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            try{
                if (!sdCardExit){
                    Toast.makeText(RecordingView.this.getContext(), "请插入SD Card",Toast.LENGTH_LONG).show();
                    return;
                }
                myRecAudioFile = File.createTempFile(strTempFile, ".amr", myRecAudioDir);
                mMediaRecorder01 = new MediaRecorder();
                mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
                mMediaRecorder01.prepare();
                mMediaRecorder01.start();
                bt_finish.setEnabled(true);
                bt_play.setEnabled(false);
                bt_delete.setEnabled(false);
                isStopRecord = false;
                (timer = new Timer()).schedule(new RecordingTimer(),0,1000);
                tv_timer.setVisibility(View.VISIBLE);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private class OnFinishButtonClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            if (myRecAudioFile != null){
                mMediaRecorder01.stop();
                adapter.add(myRecAudioFile.getName());
                mMediaRecorder01.release();
                mMediaRecorder01 = null;
                bt_finish.setEnabled(false);
                isStopRecord = true;
                counter = 0;
                timer.cancel();
                tv_timer.setVisibility(View.GONE);
            }
        }
    }
    private class OnPlayButtonClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            if (myPlayFile != null && myPlayFile.exists()) {
                openFile(myPlayFile);
            }
        }
    }
    private class OnDeleteButtonClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            if (myPlayFile != null){
                adapter.remove(myPlayFile.getName());
                if (myPlayFile.exists()){
                    myPlayFile.delete();
                }
            }
        }
    }

    private class OnListViewItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            bt_play.setEnabled(true);
            bt_delete.setEnabled(true);
            myPlayFile = new File(myRecAudioDir.getAbsolutePath() + File.separator  + ((CheckedTextView) view).getText());
        }
    }

    private class RecordingTimer extends TimerTask {
        @Override
        public void run() {
            counter ++;
            handler.sendMessage(new Message());
        }
    }


    private void openFile(File f){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        this.getContext().startActivity(intent);
    }

    private String getMIMEType(File f){
        String end = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac") || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")){
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")){
            type = "image";
        } else{
            type = "*";
        }
        type += "/*";
        return type;
    }
}
