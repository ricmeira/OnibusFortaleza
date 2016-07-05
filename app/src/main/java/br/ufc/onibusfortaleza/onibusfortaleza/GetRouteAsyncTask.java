package br.ufc.onibusfortaleza.onibusfortaleza;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufc.onibusfortaleza.onibusfortaleza.model.ApiResponse;

/**
 * Created by eduardo on 16-06-22.
 */
public class GetRouteAsyncTask extends AsyncTask<String, String, String> {
    public GetRouteAsyncTask() {
    }

    protected String doInBackground(String... params) {
        try {
            String origin = params[0];
            String dest = params[1];
            origin=origin.replace(" ","%20");
            dest=dest.replace(" ","%20");
            String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+dest+"&alternatives=true&mode=transit&key=AIzaSyAEXVH95CyyTHZE9dle3My_2J_yyo0xcxo";
            //connection
            URL url = new URL(directionsUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            //conn.setConnectTimeout(timeout);
            //conn.setReadTimeout(timeout);

            conn.connect();
            //read content
            InputStream is = conn.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); String data = null;
            String content = "";
            while ((data = reader.readLine()) != null) {
                content += data + "\n";
            }
            content.replace(" ","%20");
            parseJson(content);
            Log.e("async",content);

            //return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void onProgressUpdate(String result) {

    }

    protected void onPostExecute(String result) {

    }

    protected void parseJson(String json){
        Gson gson = new Gson();
        ApiResponse routes = gson.fromJson(json, ApiResponse.class);
    }
}
