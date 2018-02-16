package com.riantservices.riant.helpers;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.riantservices.riant.interfaces.AsyncResponse;
import com.riantservices.riant.models.DistanceDirectionTime;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {
    private GoogleMap googleMap;
    private AsyncResponse delegate;
    private float distance = 0;

    RouteParserTask(GoogleMap map, AsyncResponse delegate){
        googleMap = map;
        this.delegate = delegate;
    }

    @Override
    protected List<List<HashMap<String,String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String,String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();
            DistanceDirectionTime distanceDirectionTime = parser.parse(jObject);
            routes = distanceDirectionTime.routes;
            distance = distanceDirectionTime.distance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String,String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            List<HashMap<String,String>> path = result.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.argb(255,255,50,0));
            lineOptions.geodesic(true);
        }
        try {
            googleMap.addPolyline(lineOptions);
            delegate.processFinish(distance);
        }
        catch (Exception ignored){
        }
    }
}