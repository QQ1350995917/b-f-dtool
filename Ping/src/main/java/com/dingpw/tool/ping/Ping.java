package com.dingpw.tool.ping;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Ping {
    public static void ping(String ip){
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec("ping -c 100 -w 100 " + ip);
            inputStream = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String content = null;
            while ((content = bufferedReader.readLine()) != null){
                stringBuffer.append(content);
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
    }
}
