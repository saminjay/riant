package com.riantservices.riant;

import android.content.DialogInterface;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

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

public class CorporateBookActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Pickup, Destination, FriendContact, Time;
    private RadioButton radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9, radio10, radio11, radio12, radio13;
    private String strEmail, strBookFor, strCar, strAC, strPickup, strDestination, strNumber, strTime, strDuration;
    private String mon,tue,wed,thu,fri,sat,sun;
    private LatLng pickup,destination;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strBookFor = ""; mon="n";
        strCar = ""; tue="n";
        strAC = ""; wed="n";
        strPickup = ""; thu="n";
        strDestination = ""; fri="n";
        strNumber = ""; sat="n"; sun="n";
        setContentView(R.layout.activity_corporate_book);
        Button mon = findViewById(R.id.mon);
        Button tue = findViewById(R.id.tue);
        Button wed = findViewById(R.id.wed);
        Button thu = findViewById(R.id.thu);
        Button fri = findViewById(R.id.fri);
        Button sat = findViewById(R.id.sat);
        Button sun = findViewById(R.id.sun);
        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button1);
        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);
        sun.setOnClickListener(this);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        getSupportActionBar().hide();
        session = new SessionManager(getApplicationContext());
        strEmail = session.getEmail();
        RadioGroup radio, radioA, radioB, radioC;
        Pickup = findViewById(R.id.edit1);
        Destination = findViewById(R.id.edit2);
        FriendContact = findViewById(R.id.edit3);
        Time = findViewById(R.id.time);
        radio = findViewById(R.id.radio);
        radioA = findViewById(R.id.radioA);
        radioB = findViewById(R.id.radioB);
        radioC = findViewById(R.id.radioC);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);
        radio5 = findViewById(R.id.radio5);
        radio6 = findViewById(R.id.radio6);
        radio7 = findViewById(R.id.radio7);
        radio8 = findViewById(R.id.radio8);
        radio9 = findViewById(R.id.radio9);
        radio10 = findViewById(R.id.radio10);
        radio11 = findViewById(R.id.radio11);
        radio12 = findViewById(R.id.radio12);
        radio13 = findViewById(R.id.radio13);
        FriendContact.setVisibility(View.INVISIBLE);
        Bundle Coordinates=getIntent().getExtras();
        double[] lat=Coordinates.getDoubleArray("lat");
        double[] lng=Coordinates.getDoubleArray("lng");
        pickup=new LatLng(lat[0],lng[0]);
        destination=new LatLng(lat[1],lng[1]);
        radio.clearCheck();
        radioA.clearCheck();
        radioB.clearCheck();
        radioC.clearCheck();

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

        radioA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio3:
                        strCar = "";
                        strCar = "Hatchback";
                        radio3.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio4.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio4:
                        strCar = "";
                        strCar = "Sedan";
                        radio4.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio3.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio5:
                        strCar = "";
                        strCar = "SUV";
                        radio5.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio4.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio3.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio6:
                        strCar = "";
                        strCar = "Luxury";
                        radio6.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio4.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio3.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio7:
                        strCar = "";
                        strCar = "Limousine";
                        radio7.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio4.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio3.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                }
            }
        });

        radioB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio8:
                        strAC = "";
                        strAC = "AC";
                        radio8.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio9.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio9:
                        strAC = "";
                        strAC = "NonAC";
                        radio9.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio8.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                }
            }
        });

        radioC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio10:
                        strDuration = "";
                        strDuration = "1 Week";
                        radio10.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio11.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio12.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio13.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio11:
                        strDuration = "";
                        strDuration = "1 Month";
                        radio11.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio10.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio12.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio13.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio12:
                        strDuration = "";
                        strDuration = "3 Months";
                        radio12.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio11.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio10.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio13.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;
                    case R.id.radio13:
                        strDuration = "";
                        strDuration = "6 Months";
                        radio13.setBackgroundColor(getResources().getColor(R.color.colorLight));
                        radio11.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio12.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        radio10.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                        break;

                }
            }
        });

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    TextView textView = (TextView) v;
                    textView.setBackground(getResources().getDrawable(R.drawable.textboxselected));
                    textView.setHintTextColor(getResources().getColor(R.color.colorBlack));
                    textView.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else{
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
            case R.id.mon:
                if(mon.equals("n")) {view.setBackground(getResources().getDrawable(R.drawable.buttonselected));mon="y";}
                else {view.setBackground(getResources().getDrawable(R.drawable.buttonshape));mon="n";}
                break;
            case R.id.tue:
                if(tue.equals("n")) {view.setBackground(getResources().getDrawable(R.drawable.buttonselected));tue="y";}
                else {view.setBackground(getResources().getDrawable(R.drawable.buttonshape));tue="n";}
                break;
            case R.id.wed:
                if(wed.equals("n")) {view.setBackground(getResources().getDrawable(R.drawable.buttonselected));wed="y";}
                else {view.setBackground(getResources().getDrawable(R.drawable.buttonshape));wed="n";}
                break;
            case R.id.thu:
                if(thu.equals("n")) {view.setBackground(getResources().getDrawable(R.drawable.buttonselected));thu="y";}
                else {view.setBackground(getResources().getDrawable(R.drawable.buttonshape));thu="n";}
                break;
            case R.id.fri:
                if(fri.equals("n")) {view.setBackground(getResources().getDrawable(R.drawable.buttonselected));fri="y";}
                else {view.setBackground(getResources().getDrawable(R.drawable.buttonshape));fri="n";}
                break;
            case R.id.sat:
                if(sat.equals("n")) {view.setBackground(getResources().getDrawable(R.drawable.buttonselected));sat="y";}
                else {view.setBackground(getResources().getDrawable(R.drawable.buttonshape));sat="n";}
                break;
            case R.id.sun:
                if(sun.equals("n")) {view.setBackground(getResources().getDrawable(R.drawable.buttonselected));sun="y";}
                else {view.setBackground(getResources().getDrawable(R.drawable.buttonshape));sun="n";}
                break;
            case R.id.button1:
                strPickup = Pickup.getText().toString();
                strDestination = Destination.getText().toString();
                strNumber = FriendContact.getText().toString();
                strTime = Time.getText().toString();
                if (strPickup.matches("")) {
                    alertDialog("Please enter your Pickup Location");
                } else if (strDestination.matches("")) {
                    alertDialog("Please enter your Destination");
                } else if (strBookFor.matches("")) {
                    alertDialog("Please choose who are you booking the ride for.");
                } else if (radio2.isChecked() && strNumber.matches("")) {
                    alertDialog("Please enter friend's contact number.");
                } else if (strTime.matches("")) {
                    alertDialog("Please select the time of arrival.");
                } else if (mon.equals("n")&&tue.equals("n")&&wed.equals("n")&&thu.equals("n")&&fri.equals("n")&&sat.equals("n")&&sun.equals("n")) {
                    alertDialog("Please select atleast one working day.");
                } else if (strCar.matches("")) {
                    alertDialog("Please choose a Car Type");
                } else if (strAC.matches("")) {
                    alertDialog("Please choose between AC or Non-AC");
                } else if (strDuration.matches("")) {
                    alertDialog("Please select the duration for which you want the service");
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


    protected void Book()throws UnsupportedEncodingException{
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
                    json.put("pickupCoordinate",pickup);
                    json.put("destination", strDestination);
                    json.put("destinationCoordinate",destination);
                    json.put("bookFor", strBookFor);
                    json.put("number", strNumber);
                    json.put("time", strTime);
                    json.put("ac", strAC);
                    json.put("car", strCar);
                    json.put("duration", strDuration);
                    json.put("mon", mon);
                    json.put("tue", tue);
                    json.put("wed", wed);
                    json.put("thu", thu);
                    json.put("fri", fri);
                    json.put("sat", sat);
                    json.put("sun", sun);
                    StringEntity se = new StringEntity( json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        respond(in);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    alertDialog("Error: Cannot Estabilish Connection");
                }

                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();
    }

    public void respond(InputStream in)throws JSONException {
        JSONObject result=new JSONObject(in.toString());

        if(result.getInt("status")==1){
            alertDialog("Booking Successful");

        }
        else{
            alertDialog("System error, please contact with administrator");
        }
    }
}