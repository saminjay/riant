package com.riantservices.riant.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.riantservices.riant.helpers.DirectionsJSONParser;
import com.riantservices.riant.helpers.GPSTracker;
import com.riantservices.riant.R;
import com.riantservices.riant.helpers.SessionManager;
import com.riantservices.riant.helpers.SingleShotLocationProvider;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    static GoogleMap googleMap;
    private Marker userMarker;
    SessionManager session;
    boolean pickupMarked = false;
    private LatLng pickup;
    private List<LatLng> destination;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        session = new SessionManager(getApplicationContext());
        ImageButton[] icons = new ImageButton[5];
        TextView[] iconText = new TextView[5];
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        ImageButton bar = findViewById(R.id.bar);
        bar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START,true);
            }
        });
        Button expandButton = findViewById(R.id.expand);
        expandButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START,true);
            }
        });
        OnClickListener iconClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.account1:
                    case R.id.textAccount1:
                        Intent goAccount = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(goAccount);
                        break;
                    case R.id.trips1:
                    case R.id.textTrips1:
                        Intent goTrips = new Intent(MainActivity.this, TripsActivity.class);
                        startActivity(goTrips);
                        break;
                    case R.id.notifictions1:
                    case R.id.textNotifications1:
                        alertDialog("Notifications is clicked");
                        break;
                    case R.id.settings1:
                    case R.id.textSettings1:
                        alertDialog("Settings is clicked");
                        break;
                    case R.id.help1:
                    case R.id.textHelp1:
                        alertDialog("Help is clicked");
                        break;
                }
            }
        };
        icons[0] = findViewById(R.id.account1);
        icons[1] = findViewById(R.id.trips1);
        icons[2] = findViewById(R.id.settings1);
        icons[3] = findViewById(R.id.notifictions1);
        icons[4] = findViewById(R.id.help1);
        for (int i = 0; i < 5; i++) icons[i].setOnClickListener(iconClickListener);
        iconText[0] = findViewById(R.id.textAccount1);
        iconText[1] = findViewById(R.id.textTrips1);
        iconText[2] = findViewById(R.id.textNotifications1);
        iconText[3] = findViewById(R.id.textSettings1);
        iconText[4] = findViewById(R.id.textHelp1);
        for (int i = 0; i < 5; i++) iconText[i].setOnClickListener(iconClickListener);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        destination = new ArrayList<>();
        SearchView searchView = (findViewById(R.id.searchView));
        Button button = findViewById(R.id.proceed);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destination.size() != 0)
                    bookMenu(pickup, destination);
                else
                    alertDialog("Choose a Destination");
            }
        });
        Button button1 = findViewById(R.id.clear);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pickup=null;
                pickupMarked=false;
                destination.clear();
                map.clear();
            }
        });
        ImageButton locate = findViewById(R.id.locate);
        locate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Address> addressList = new ArrayList<>();

                if (query != null && !query.equals("")) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(query, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address;
                    if (!addressList.isEmpty()) {
                        address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(query));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setBuildingsEnabled(true);
        googleMap = map;
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mark(latLng);
            }
        });

        MarkerOptions options=new MarkerOptions().position(new LatLng(20.2961,85.8245)).title("Current Location");
        userMarker = googleMap.addMarker(options);
        userMarker.setVisible(false);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userMarker.getPosition(),15);
        googleMap.animateCamera(update);
        requestLocation();
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void requestLocation(){
        SingleShotLocationProvider.requestSingleUpdate(MainActivity.this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("loc","My cordinates:"+ location.toString());
                        LatLng CURRENT_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
                        userMarker.setPosition(CURRENT_LOCATION);
                        userMarker.setVisible(true);
                        CameraUpdate update = CameraUpdateFactory.newLatLng(CURRENT_LOCATION);
                        googleMap.animateCamera(update);
                    }
                });
    }

    protected void mark(LatLng latLng) {
        if (pickupMarked) {
            destination.add(latLng);
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).position(latLng).title("destination".concat(String.valueOf(destination.size()))));
            if (destination.size() == 1) {
                LatLng origin = pickup;
                LatLng dest = destination.get(0);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            } else {
                LatLng origin = destination.get(destination.size() - 2);
                LatLng dest = destination.get(destination.size() - 1);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        } else {
            pickup = latLng;
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLng).title("pickup"));
            pickupMarked = true;
        }
    }

    protected void bookMenu(LatLng pickup, List<LatLng> destination) {
        int i;
        //float d=0;
        //float[] res =new float[3];
        //Location.distanceBetween(pickup.latitude,pickup.longitude,destination.get(0).latitude,destination.get(0).longitude,res);
        //d+=res[0];
        //for(i=1;i<destination.size();i--){
        //    Location.distanceBetween(destination.get(i-1).latitude,destination.get(i-1).longitude,destination.get(i).latitude,destination.get(i).longitude,res);
        //    d+=res[0];
        //}
        //alertDialog(Float.toString(d));
        i=0;
        final Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        Bundle bundle = new Bundle();
        double[] lat = new double[destination.size() + 1];
        double[] lng = new double[destination.size() + 1];
        lat[i] = pickup.latitude;
        lng[i] = pickup.longitude;
        for (LatLng t : destination) {
            i++;
            lat[i] = t.latitude;
            lng[i] = t.longitude;
        }
        bundle.putDoubleArray("lat", lat);
        bundle.putDoubleArray("lng", lng);
        intent.putExtra("Coordinates", bundle);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        startActivity(intent);
    }

    protected void UpdateGPS(final String Email) throws UnsupportedEncodingException {
        GPSTracker gps = new GPSTracker(this);
        final double lat = gps.getLatitude();
        final double lon = gps.getLongitude();
        userMarker.setPosition(new LatLng(lat,lon));
        Thread t = new Thread() {
            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost("url");
                    json.put("email", Email);
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

            /*Checking response */
                    if (response != null) {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        respond(in);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    alertDialog("Error: Cannot Estabilish Connection");
                }
                Looper.loop(); //Loop in the message queue
            }
        };
        try {
            t.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.start();
    }

    public void alertDialog(String Message) {

        new AlertDialog.Builder(this).setTitle("Riant Alert").setMessage(Message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public void respond(InputStream in) throws JSONException {
        final JSONObject result = new JSONObject(in.toString());
        Thread t = new Thread(){
            public void run(){
                try {
                    JSONObject resultArrayJson;
                    String Latitude,Longitude,CabType;
                    double Lat,Lon;
                    LatLng Taxi_Location;
                    JSONObject StatusRequest = result.getJSONObject("result");
                    JSONArray TaxiDrivers = StatusRequest.getJSONArray("TaxiDrivers");
                    int lenArray = TaxiDrivers.length();
                    for (int i = 0; i < lenArray; i++) {
                        resultArrayJson = TaxiDrivers.getJSONObject(i);
                        Latitude = resultArrayJson.getString("Latitude");
                        Longitude = resultArrayJson.getString("Longitude");
                        CabType = resultArrayJson.getString("CabType");
                        Lat = Double.parseDouble(Latitude);
                        Lon = Double.parseDouble(Longitude);
                        Taxi_Location = new LatLng(Lat, Lon);
                        googleMap.addMarker(new MarkerOptions()
                                .position(Taxi_Location)
                                .title("Riant " + CabType)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar)));
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        String output = "json";
        return  "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    static class DownloadTask extends AsyncTask<String,Integer,String> {

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

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

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

    static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String,String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String,String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
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
            }
            catch (Exception ignored){

            }
        }
    }
}