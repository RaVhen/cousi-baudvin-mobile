package com.example.JsonR;

import android.app.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.*;
import android.content.*;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.*;
import android.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.sql.ParameterMetaData;

import static android.graphics.BitmapFactory.*;

public class SimpleIntentService extends IntentService{

    public static final String JSON = "com.example.JsonR.JSON";
    public static final String DONE = "com.example.JsonR.DONE";
    private Bitmap bmp;
    public SimpleIntentService() {
        super("SimpleIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String dataString = intent.getStringExtra("request");
            try{
                Log.d("URL", dataString);
                String result = downloadUrl(dataString);
                longInfo(result);
                Intent i = new Intent();
                i.setAction(DONE);
                i.putExtra("JSON", result);
                sendBroadcast(i);
            }catch (IOException e){
                Log.d("IOExep", "erreur dans le DLintent service 1"+e.getMessage());
            }
        }
    }

    private String downloadUrl(String myUrl) throws IOException{
        InputStream is = null;
        HttpURLConnection connec = null;
        String contentAsString = null;
        try{
            URL url = new URL(myUrl);
            connec = (HttpURLConnection) url.openConnection();
            connec.setRequestMethod("GET");
            connec.connect();
            is = connec.getInputStream();
            contentAsString = convertStreamToString(is);
            is.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                is.close();
            }
            connec.disconnect();
            Log.d("INFO", "Disconnected from server");
        }
        return(contentAsString);
    }

    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.d("", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.d("", str);
    }

    private String convertStreamToString(java.io.InputStream is) throws IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        JSONObject obj = new JSONObject(responseStrBuilder.toString());
        return obj.toString();
    }

}