package com.riantservices.riant.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.riantservices.riant.R;
import com.riantservices.riant.helpers.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StationBookActivity extends AppCompatActivity implements View.OnClickListener {
    SessionManager session;
    ImageButton oneway, roundtrip;
    Button AC, NonAC;
    private EditText Pickup, Destination, FriendContact;
    private RadioButton radio2;
    private String strEmail, strBookFor, strTrip, strAC, strPickup, strDestination, strNumber;
    private LatLng pickup, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strBookFor = "";
        strTrip = "";
        strAC = "";
        strPickup = "";
        strDestination = "";
        strNumber = "";
        setContentView(R.layout.activity_station_book);
        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button1);
        oneway = findViewById(R.id.oneway);
        roundtrip = findViewById(R.id.roundtrip);
        AC = findViewById(R.id.AC);
        NonAC = findViewById(R.id.NonAC);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        oneway.setOnClickListener(this);
        roundtrip.setOnClickListener(this);
        AC.setOnClickListener(this);
        NonAC.setOnClickListener(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        session = new SessionManager(getApplicationContext());
        strEmail = session.getEmail();
        RadioGroup radio;
        Pickup = findViewById(R.id.edit1);
        Destination = findViewById(R.id.edit2);
        FriendContact = findViewById(R.id.edit3);
        radio = findViewById(R.id.radio);
        radio2 = findViewById(R.id.radio2);
        FriendContact.setVisibility(View.INVISIBLE);
        Bundle Coordinates = getIntent().getBundleExtra("Coordinates");
        if (Coordinates != null) {
            double[] lat = Coordinates.getDoubleArray("lat");
            double[] lng = Coordinates.getDoubleArray("lng");
            if (lat != null && lng != null) {
                pickup = new LatLng(lat[0], lng[0]);
                destination = new LatLng(lat[1], lng[1]);
            }
        }
        radio.clearCheck();
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        strBookFor = "";
                        strBookFor = "For Yourself";
                        FriendContact.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radio2:
                        strBookFor = "";
                        strBookFor = "Friend";
                        FriendContact.setText("");
                        FriendContact.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TextView textView = (TextView) v;
                    textView.setBackground(getResources().getDrawable(R.drawable.textboxselected));
                    textView.setHintTextColor(getResources().getColor(R.color.colorBlack));
                    textView.setTextColor(getResources().getColor(R.color.colorBlack));
                } else {
                    TextView textView = (TextView) v;
                    textView.setBackground(getResources().getDrawable(R.drawable.textboxborder));
                    textView.setHintTextColor(getResources().getColor(R.color.colorWhite));
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            }
        };

        Pickup.setOnFocusChangeListener(onFocusChangeListener);
        Destination.setOnFocusChangeListener(onFocusChangeListener);
        FriendContact.setOnFocusChangeListener(onFocusChangeListener);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oneway:
                oneway.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                roundtrip.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strTrip = "";
                strTrip = "Oneway";
                break;
            case R.id.roundtrip:
                roundtrip.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                oneway.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strTrip = "";
                strTrip = "Roundtrip";
                break;
            case R.id.AC:
                AC.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                NonAC.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strAC = "";
                strAC = "AC";
                break;
            case R.id.NonAC:
                NonAC.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                AC.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strAC = "";
                strAC = "NonAC";
                break;
            case R.id.button1:
                strPickup = Pickup.getText().toString();
                strDestination = Destination.getText().toString();
                strNumber = FriendContact.getText().toString();
                if (strPickup.matches("")) {
                    alertDialog("Please enter your Pickup Location");
                } else if (strDestination.matches("")) {
                    alertDialog("Please enter your Destination");
                } else if (strBookFor.matches("")) {
                    alertDialog("Please choose who are you booking the ride for.");
                } else if (radio2.isChecked() && strNumber.matches("")) {
                    alertDialog("Please enter friend's contact number.");
                } else if (strTrip.matches("")) {
                    alertDialog("Please choose between one way or round trip");
                } else if (strAC.matches("")) {
                    alertDialog("Please choose between AC or Non-AC");
                } else {
                    try {
                        Book();
                    } catch (UnsupportedEncodingException e) {
                        alertDialog("Unsupported Encoding");
                    }
                }
                break;
            case R.id.button:
                //estimate fare function
                break;
        }
    }

    public void alertDialog(String Message) {

        new AlertDialog.Builder(this).setTitle("Riant Alert").setMessage(Message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    protected void Book() throws UnsupportedEncodingException {
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost("url");
                    json.put("email", strEmail);
                    json.put("pickup", strPickup);
                    json.put("pickupCoordinate", pickup);
                    json.put("destination", strDestination);
                    json.put("destinationCoordinate", destination);
                    json.put("bookFor", strBookFor);
                    json.put("number", strNumber);
                    json.put("ac", strAC);
                    json.put("trip", strTrip);
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

        t.start();
    }

    public void respond(InputStream in) throws JSONException {
        JSONObject result = new JSONObject(in.toString());

        if (result.getInt("status") == 1) {
            alertDialog("Booking Successful");

        } else {
            alertDialog("System error, please contact with administrator");
        }
    }
}
