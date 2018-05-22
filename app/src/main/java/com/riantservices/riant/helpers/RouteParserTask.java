package com.riantservices.riant.helpers;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
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
    private Polyline greenPolyLine;
    private Polyline orangePolyLine;
    private ArrayList<LatLng> listLatLng = new ArrayList<>();

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

            this.listLatLng.addAll(points);

            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.argb(255,0,255,0));
            lineOptions.jointType(JointType.ROUND);
            lineOptions.geodesic(true);
        }
        try {
            greenPolyLine = googleMap.addPolyline(lineOptions);
            assert lineOptions != null;
            lineOptions.color(Color.argb(255,255,50,0));
            orangePolyLine = googleMap.addPolyline(lineOptions);
            animatePolyLine();
            delegate.processFinish(distance);
        }
        catch (Exception ignored){
        }
    }

    private void animatePolyLine() {

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                List<LatLng> latLngList = greenPolyLine.getPoints();
                int initialPointSize = latLngList.size();
                int animatedValue = (int) animator.getAnimatedValue();
                int newPoints = (animatedValue * listLatLng.size()) / 100;

                if (initialPointSize < newPoints ) {
                    latLngList.addAll(listLatLng.subList(initialPointSize, newPoints));
                    greenPolyLine.setPoints(latLngList);
                }


            }
        });

        animator.addListener(polyLineAnimationListener);
        animator.start();
    }

    private Animator.AnimatorListener polyLineAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {

            List<LatLng> greenLatLng = greenPolyLine.getPoints();
            List<LatLng> orangeLatLng = orangePolyLine.getPoints();

            orangeLatLng.clear();
            orangeLatLng.addAll(greenLatLng);
            greenLatLng.clear();

            greenPolyLine.setPoints(greenLatLng);
            orangePolyLine.setPoints(orangeLatLng);

            greenPolyLine.setZIndex(2);

            animator.start();
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {


        }
    };
}