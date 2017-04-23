package com.example.ami.mortgagecalculator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sam on 3/18/17.
 */

public class LatLngTask extends AsyncTask {

    private ProgressDialog dialog ;

    public LatLngTask(Activity act){
        dialog = new ProgressDialog(act);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        //1600+Amphitheatre+Parkway,+Mountain+View,+CA
        Log.d("asynctask", params[0].toString());
        HttpGet httpGet = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address="+params[0].toString()+"&key="+params[1].toString()+"");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
