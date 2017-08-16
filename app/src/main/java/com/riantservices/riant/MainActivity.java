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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

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
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        session = new SessionManager(getApplicationContext());
        ImageButton[] icons=new ImageButton[10];
        TextView[] iconText=new TextView[5];
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final SearchView searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        final int longClickDuration = 5000; //for long click to trigger after 5 seconds

        ImageButton sos=(ImageButton)findViewById(R.id.sos);
        sos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long then=0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                     then = (long) System.currentTimeMillis();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:100"));
                        if(ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.CALL_PHONE ) == PackageManager.PERMISSION_GRANTED)
                           {alertDialog("SOS activated. Calling 100.");startActivity(intent);}
                        else
                            alertDialog("SOS failed");
                        return false;
                    }
                }
                return true;
            }
        });
        ImageButton bar=(ImageButton)findViewById(R.id.bar);
        bar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view2.getVisibility()==View.INVISIBLE) {
                    view2.setVisibility(View.VISIBLE);
                } else {
                    view2.setVisibility(View.INVISIBLE);
                }
            }
        });
        OnClickListener iconClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.account:
                    case R.id.account1:
                    case R.id.textAccount1:
                        alertDialog("Account is clicked"); break;
                    case R.id.trips:
                    case R.id.trips1:
                    case R.id.textTrips1:
                        Intent goTrips = new Intent(getApplicationContext(),TripsActivity.class);
                        startActivity(goTrips);
                        break;
                    case R.id.notifictions:
                    case R.id.notifictions1:
                    case R.id.textNotifications1:
                        alertDialog("Notifications is clisked"); break;
                    case R.id.settings:
                    case R.id.settings1:
                    case R.id.textSettings1:
                        alertDialog("Settings is clicked"); break;
                    case R.id.help:
                    case R.id.help1:
                    case R.id.textHelp1:
                        alertDialog("Help is clicked"); break;
                }
            }
        };
        icons[0]=(ImageButton)findViewById(R.id.account);
        icons[1]=(ImageButton)findViewById(R.id.account1);
        icons[2]=(ImageButton)findViewById(R.id.trips);
        icons[3]=(ImageButton)findViewById(R.id.trips1);
        icons[4]=(ImageButton)findViewById(R.id.settings);
        icons[5]=(ImageButton)findViewById(R.id.settings1);
        icons[6]=(ImageButton)findViewById(R.id.notifictions);
        icons[7]=(ImageButton)findViewById(R.id.notifictions1);
        icons[8]=(ImageButton)findViewById(R.id.help);
        icons[9]=(ImageButton)findViewById(R.id.help1);
        for(int i=0;i<10;i++)icons[i].setOnClickListener(iconClickListener);
        iconText[0]=(TextView)findViewById(R.id.textAccount1);
        iconText[1]=(TextView)findViewById(R.id.textTrips1);
        iconText[2]=(TextView)findViewById(R.id.textNotifications1);
        iconText[3]=(TextView)findViewById(R.id.textSettings1);
        iconText[4]=(TextView)findViewById(R.id.textHelp1);
        for(int i=0;i<5;i++)iconText[i].setOnClickListener(iconClickListener);
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

}