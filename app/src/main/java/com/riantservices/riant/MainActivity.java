package com.riantservices.riant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static java.lang.Thread.sleep;

public class MainActivity extends FragmentActivity implements OnClickListener,OnMapReadyCallback {

    GoogleMap googleMap;
    Marker UserMarker;
    private String response;
    SessionManager session;
    RelativeLayout view1,view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1 = (RelativeLayout) findViewById(R.id.menu);
        view2 = (RelativeLayout) findViewById(R.id.extended);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        session = new SessionManager(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bar:
                if (view1.getVisibility()==View.INVISIBLE) {
                  Animation getIn = generateMoveLeftAnimation(view1);
                  getIn.start();
                } else {
                  Animation getOut = generateMoveRightAnimation(view1);
                  getOut.start();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        double Lat, Lon;
        String UserEmail;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setBuildingsEnabled(true);
        googleMap = map;
        GPSTracker gps = new GPSTracker(this);
        UserEmail = session.getEmail();
        Lat = gps.getLatitude();
        Lon = gps.getLongitude();
        LatLng CURRENT_LOCATION = new LatLng(Lat, Lon);
        UserMarker.setPosition(CURRENT_LOCATION);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 15);
        googleMap.animateCamera(update);
        String perm[] = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            googleMap.setMyLocationEnabled(true);
            try{
                UpdateGPS(UserEmail);
            }
            catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

        }
        else if (Build.VERSION.SDK_INT < 23) {
            googleMap.setMyLocationEnabled(true);
            try{
                UpdateGPS(UserEmail);
            }
            catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        else
            requestPermissions(perm, 1);
    }

    protected void UpdateGPS(final String Email) throws UnsupportedEncodingException {
        GPSTracker gps = new GPSTracker(this);
        final double lat = gps.getLatitude();
        final double lon = gps.getLongitude();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        String data = URLEncoder.encode("lat", "UTF-8")
                                + "=" + URLEncoder.encode(String.valueOf(lat), "UTF-8") + "&" +
                                URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(lon), "UTF-8") + "&"
                                + URLEncoder.encode("UserEmail", "UTF-8")
                                + "=" + URLEncoder.encode(Email, "UTF-8");
                        java.net.URL url = new URL("http://riantservices.com/App_Data/updateGPS.php");
                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write(data);
                        wr.flush();
                        response = slurp(conn.getInputStream());
                        respond(response);
                    } catch (Exception ex) {
                        alertDialog("Error in Connection. Please try later.");
                    }
                }
            };
            sleep(2000);
            t.start();
        } catch (InterruptedException e) {
            Log.e("developer","Thread updating GPS is interrupted");
        }
    }

    public void alertDialog(String Message) {

        new AlertDialog.Builder(this).setTitle("Riant Alert").setMessage(Message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public static String slurp(final InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    public void respond(final String response) throws IOException {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject reader;
                    try {
                        reader = new JSONObject(response);
                        JSONObject StatusRequest = reader.getJSONObject("Result");
                        JSONArray TaxiDrivers = StatusRequest.getJSONArray("TaxiDrivers");
                        int lenArray = TaxiDrivers.length();
                        for (int i = 0; i < lenArray; i++) {
                            JSONObject resultArrayJson = TaxiDrivers.getJSONObject(i);
                            String Latitude = resultArrayJson.getString("Latitude");
                            String Longitude = resultArrayJson.getString("Longitude");
                            String CabNo = resultArrayJson.getString("CabNo");
                            String CabType = resultArrayJson.getString("CabType");
                            double Lat = Double.parseDouble(Latitude);
                            double Lon = Double.parseDouble(Longitude);
                            LatLng Taxi_Location = new LatLng(Lat, Lon);
                                googleMap.addMarker(new MarkerOptions()
                                        .position(Taxi_Location)
                                        .title(CabType)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar))
                                );
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //private void sos() {}

    private Animation generateMoveRightAnimation(final View view) {
        final Animation animation = new TranslateAnimation(Animation.ABSOLUTE, -view.getWidth(), Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // change the coordinates of the view object itself so that on click listener reacts to new position
                //view.layout(view.getLeft()+view.getWidth(), view.getTop(), view.getRight()+view.getWidth(), view.getBottom());
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    private Animation generateMoveLeftAnimation(final View view) {
        final Animation animation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -view.getWidth(),
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // change the coordinates of the view object itself so that on click listener reacts to new position
                //view.layout(view.getLeft()-view.getWidth(), view.getTop(), view.getRight()-view.getWidth(), view.getBottom());
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { view.setVisibility(View.INVISIBLE);}
        });
        return animation;
    }
}