package com.dingpw.tool.ping;

import android.os.AsyncTask;
import com.dingpw.djson.Djson;
import com.dingpw.djson.DjsonArray;
import com.dingpw.djson.DjsonObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PingTask extends AsyncTask<DjsonObject, DjsonObject, DjsonObject> {

    private IPingCallBack iPingCallBack = null;
    public PingTask(IPingCallBack iPingCallBack) {
        this.iPingCallBack = iPingCallBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.iPingCallBack.onStart(null);
    }

    @Override
    protected void onPostExecute(DjsonObject djsonObject) {
        super.onPostExecute(djsonObject);
        this.iPingCallBack.onEnd(djsonObject);
    }

    @Override
    protected DjsonObject doInBackground(DjsonObject... params) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec("ping -c 5 -w 5 " + params[0].getString("ip"));
            inputStream = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            DjsonObject jsonObject = Djson.createJsonObject();
            DjsonArray jsonArray = Djson.createJsonArray();
            String content = null;
            while ((content = bufferedReader.readLine()) != null){
                jsonArray.push(content);
                publishProgress(jsonObject.set("message",jsonArray));
            }
        }catch (Exception e){

        }finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (Exception e){

            }
        }
        return null;
    }




    @Override
    protected void onProgressUpdate(DjsonObject... values) {
        super.onProgressUpdate(values);
        this.iPingCallBack.onProgressing(values[0]);
    }

}
