package com.riantservices.riant.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.riantservices.riant.interfaces.AsyncResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadRouteTask extends AsyncTask<String,Integer,String> {

    private GoogleMap googleMap;
    private AsyncResponse delegate;

    public DownloadRouteTask(GoogleMap map, AsyncResponse delegate){
        googleMap = map;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... url) {

        String data = "";

        try {
            data = downloadUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        RouteParserTask routeParserTask = new RouteParserTask(googleMap,delegate);
        routeParserTask.execute(result);

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            if(iStream!=null) iStream.close();
            if(urlConnection!=null) urlConnection.disconnect();
        }
        return data;
    }
}
