package com.example.JsonR;

import android.app.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.*;
import android.content.*;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.*;

public class MyMainActivity extends Activity
{
    /** Called when the activity is first created. */
    private ResponseReceiver receiver;
    private IntentFilter filter;
    private ArrayList<String> postName = new ArrayList<String>();
    private ArrayList<String> bitmapArray = new ArrayList<String>();

    private String url = "http://infinigag.eu01.aws.af.cm/";
    private TextView Name, Description, Votes;
    private ImageView Img;
    private ListView Lv;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        filter = new IntentFilter(SimpleIntentService.DONE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        Name = (TextView)findViewById(R.id.Name);
        Img = (ImageView)findViewById(R.id.Image);
        Lv = (ListView)findViewById(R.id.listView);
        context = this;
        //Lv.setAdapter(new CustomAdapter(this, planets));

        Intent jsonM = new Intent(this, SimpleIntentService.class);
        jsonM.putExtra("request", url);
        Log.d("Intent", jsonM.toString());
        this.startService(jsonM);


    }


    public class ResponseReceiver extends BroadcastReceiver {
	    

	    public void onReceive(Context context, Intent intent) {
	    	String result = intent.getStringExtra("JSON");
            String name, description, Votes;
            JSONObject tmp;

            // JSON PARSING
            try {
                JSONObject jobject = new JSONObject(result);
                JSONArray jArray = jobject.getJSONArray("data");
                for (int i=0; i < jArray.length(); i++) {
                    try {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        name = oneObject.getString("caption");
                        tmp = new JSONObject(oneObject.getString("images"));
                        description = tmp.getString("normal");
                        postName.add(name);
                        //Name.setText(name);

                        // Get image from url
                        //new DownloadImageTask((ImageView) findViewById(R.id.Image)).execute(description);

                        bitmapArray.add(description);
                        //Img.setImageBitmap(image);

                    } catch (JSONException e) {
                        Log.e("ERROR", e.getMessage());
                    }
                }
                Lv.setAdapter(new CustomAdapter(context, postName, bitmapArray));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
	}

    // ASYNTASK: Download image and display
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}





