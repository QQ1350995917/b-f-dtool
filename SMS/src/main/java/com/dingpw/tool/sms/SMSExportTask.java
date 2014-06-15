package com.dingpw.tool.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import com.dingpw.dhelper.DateTimeUtils;
import com.dingpw.djson.Djson;
import com.dingpw.djson.DjsonObject;

import java.io.FileWriter;

/**
 * Created by dingpw on 6/14/14.
 */
public class SMSExportTask extends AsyncTask<DjsonObject, DjsonObject, DjsonObject> implements SMSConstant{

    private Context context = null;
    private ISmsCallback iSmsCallback = null;

    public SMSExportTask(Context context,ISmsCallback iSmsCallback) {
        super();
        this.context = context;
        this.iSmsCallback = iSmsCallback;
    }

    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.iSmsCallback.onStart(Djson.createJsonObject().set(KEY_PREEXECUTE,"preparing"));
    }

    /**
     * 这个方法的参数对应AsyncTask中的第一个参数
     * 这个方法的返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    @Override
    protected DjsonObject doInBackground(DjsonObject... params) {
        DjsonObject djsonObject = Djson.createJsonObject();
        String phoneNumber =  params[0] == null ? "":params[0].getString(KEY_PHONE_NUMBER);
        Cursor cursor = null;
        int cursorConut = 0;
        int index = 0;
        try {
            ContentResolver conResolver = context.getContentResolver();
            String[] projection = new String[] {ADDRESS,PERSON,DATE,PROTOCOL,READ,STATUS,TYPE,REPLY_PATH_PRESENT,BODY,LOCKED,ERROR_CODE,SEEN};
            Uri uri = Uri.parse(SMS_URI_ALL);
            if(phoneNumber.length() == 0){
                cursor = conResolver.query(uri, projection, null, null, "_id asc");
            }else{
                cursor = conResolver.query(uri, projection, "address = ?", new String[]{phoneNumber}, "_id asc");
            }
            cursorConut = cursor.getCount();
            publishProgress(Djson.createJsonObject().set(KEY_STATUS,"prepared").set(KEY_TOTAL,cursorConut));
            if (cursor.moveToFirst()) {
                String path = context.getString(R.string.string_export_dir) + phoneNumber + "_" + DateTimeUtils.getCurrentDateTimeWithoutDelimiter() + ".json";
                FileWriter writer = new FileWriter(path);
                try {
                    // 查看数据库sms表得知 subject和service_center始终是null所以这里就不获取它们的数据
                    do {
                        // 如果address == null，xml文件中是不会生成该属性的,为了保证解析时，属性能够根据索引一一对应，必须要保证所有的item标记的属性数量和顺序是一致的
                        // ，==2是发件箱;read=0表示未读，read=1表示读过，seen=0表示未读，seen=1表示读过
                        DjsonObject jsonObject = Djson.createJsonObject();
                        String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                        jsonObject.set(ADDRESS,address);
                        String person = cursor.getString(cursor.getColumnIndex(PERSON));
                        jsonObject.set(PERSON,person);
                        String date = cursor.getString(cursor.getColumnIndex(DATE));
                        jsonObject.set(DATE,date);
                        String protocol = cursor.getString(cursor.getColumnIndex(PROTOCOL));
                        jsonObject.set(PROTOCOL,protocol);
                        String read = cursor.getString(cursor.getColumnIndex(READ));
                        jsonObject.set(READ,read);
                        String status = cursor.getString(cursor.getColumnIndex(STATUS));
                        jsonObject.set(STATUS,status);
                        String type = cursor.getString(cursor.getColumnIndex(TYPE));
                        jsonObject.set(TYPE,type);
                        String reply_path_present = cursor.getString(cursor.getColumnIndex(REPLY_PATH_PRESENT));
                        jsonObject.set(REPLY_PATH_PRESENT,reply_path_present);
                        String body = cursor.getString(cursor.getColumnIndex(BODY));
                        jsonObject.set(BODY,body);
                        String locked = cursor.getString(cursor.getColumnIndex(LOCKED));
                        jsonObject.set(LOCKED,locked);
                        String error_code = cursor.getString(cursor.getColumnIndex(ERROR_CODE));
                        jsonObject.set(ERROR_CODE,error_code);
                        String seen = cursor.getString(cursor.getColumnIndex(SEEN));
                        jsonObject.set(SEEN, seen);
                        writer.write(jsonObject.toString() + "\r\n");
                        publishProgress(djsonObject.set(KEY_STATUS, "progressing").set(KEY_TOTAL, cursorConut).set(KEY_INDEX, index++).set(KEY_SMS, jsonObject).set(KEY_SERIALIZATION, true));
                    }while (cursor.moveToNext());
                }catch (Exception e){
                    publishProgress(djsonObject.set(KEY_STATUS,"error").set(KEY_TOTAL,cursorConut).set(KEY_INDEX,index).set(KEY_SERIALIZATION,false).set(KEY_ERROR_MESSAGE,e.getMessage()));
                    this.cancel(true);
                }finally {
                    if(writer != null){
                        writer.flush();
                        writer.close();
                    }
                }
            }
        }catch (Exception e){
            publishProgress(djsonObject.set(KEY_STATUS,"error").set(KEY_TOTAL,cursorConut).set(KEY_INDEX,index).set(KEY_SERIALIZATION,false).set(KEY_ERROR_MESSAGE,e.getMessage()));
            this.cancel(true);
        }finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return djsonObject.set(KEY_TOTAL,cursorConut);
    }

    /**
     * 这个方法的参数对应AsyncTask中的第三个参数（也是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    @Override
    protected void onPostExecute(DjsonObject djsonObject) {
        super.onPostExecute(djsonObject);
        this.iSmsCallback.onEnd(djsonObject);
    }

    /**
     * 这个方法的参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     */
    @Override
    protected void onProgressUpdate(DjsonObject... values) {
        super.onProgressUpdate(values);
        this.iSmsCallback.onProgressing(values[0]);
    }

    @Override
    protected void onCancelled(DjsonObject djsonObject) {
        super.onCancelled(djsonObject);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
