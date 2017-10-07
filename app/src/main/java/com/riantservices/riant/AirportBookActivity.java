package com.riantservices.riant;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import static com.riantservices.riant.R.color.colorBlack;
import static com.riantservices.riant.R.color.colorTransparent;
import static com.riantservices.riant.R.color.colorWhite;

public class AirportBookActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Pickup,Destination,FriendContact;
    private RadioButton radio2;
    private String strEmail,strBookFor,strTrip,strAC,strPickup, strDestination,strNumber;
    private LatLng pickup,destination;
    SessionManager session;
    ImageButton oneway,roundtrip;
    Button AC,NonAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strBookFor="";strTrip="";strAC="";strPickup="";strDestination="";strNumber="";
        float[] temp;
        temp=getIntent().getFloatArrayExtra("pickupLoc");
        pickup=new LatLng(temp[0],temp[1]);
        temp=getIntent().getFloatArrayExtra("destinationLoc");
        destination=new LatLng(temp[0],temp[1]);
        setContentView(R.layout.activity_airport_book);
        Button button=(Button)findViewById(R.id.button);
        Button button1=(Button)findViewById(R.id.button1);
        oneway=(ImageButton)findViewById(R.id.oneway);
        roundtrip=(ImageButton)findViewById(R.id.roundtrip);
        AC =(Button)findViewById(R.id.AC);
        NonAC=(Button)findViewById(R.id.NonAC);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        oneway.setOnClickListener(this);
        roundtrip.setOnClickListener(this);
        AC.setOnClickListener(this);
        NonAC.setOnClickListener(this);
        getSupportActionBar().hide();
        session=new SessionManager(getApplicationContext());
        strEmail=session.getEmail();
        RadioGroup radio;
        Pickup=(EditText)findViewById(R.id.edit1);
        Destination=(EditText)findViewById(R.id.edit2);
        FriendContact=(EditText)findViewById(R.id.edit3);
        radio=(RadioGroup)findViewById(R.id.radio);
        radio2=(RadioButton)findViewById(R.id.radio2);
        FriendContact.setVisibility(View.INVISIBLE);
        getAddress();
        radio.clearCheck();
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.radio1:
                        strBookFor="";
                        strBookFor="For Yourself";
                        FriendContact.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radio2:
                        strBookFor="";
                        strBookFor="Friend";
                        FriendContact.setText("");
                        FriendContact.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    TextView textView = (TextView) v;
                    textView.setBackgroundColor(getResources().getColor(colorWhite));
                    textView.setHintTextColor(getResources().getColor(colorBlack));
                    textView.setTextColor(getResources().getColor(colorBlack));
                }
                else{
                    TextView textView = (TextView) v;
                    textView.setBackgroundColor(getResources().getColor(colorTransparent));
                    textView.setHintTextColor(getResources().getColor(colorWhite));
                    textView.setTextColor(getResources().getColor(colorWhite));
                }
            }
        };

        Pickup.setOnFocusChangeListener(onFocusChangeListener);
        Destination.setOnFocusChangeListener(onFocusChangeListener);
        FriendContact.setOnFocusChangeListener(onFocusChangeListener);

    }

    public void getAddress(){
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        Address address;
        String result;
        try {
            List<Address> pickupAddress;
            List<Address> destinationAddress;
            pickupAddress = geocoder.getFromLocation(pickup.latitude, pickup.longitude, 1);
            destinationAddress = geocoder.getFromLocation(destination.latitude, destination.longitude, 1);
            if(pickupAddress.size()>0){
                address=pickupAddress.get(0);
                result="";
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    result+=(address.getAddressLine(i)+" ");
                Pickup.setText(result);
            }
            if(destinationAddress.size()>0){
                address=destinationAddress.get(0);
                result="";
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    result+=(address.getAddressLine(i)+" ");
                Destination.setText(result);
            }
        }
        catch (IOException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oneway:
                oneway.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                roundtrip.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strTrip="";
                strTrip="Oneway";
                break;
            case R.id.roundtrip:
                roundtrip.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                oneway.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strTrip="";
                strTrip="Roundtrip";
                break;
            case R.id.AC:
                AC.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                NonAC.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strAC="";
                strAC="AC";
                break;
            case R.id.NonAC:
                NonAC.setBackground(getResources().getDrawable(R.drawable.buttonselected));
                AC.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                strAC="";
                strAC="NonAC";
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
                } else if (radio2.isChecked()&&strNumber.matches("")) {
                    alertDialog("Please enter friend's contact number.");
                } else if(strTrip.matches("")) {
                    alertDialog("Please choose between one way or round trip");
                } else if(strAC.matches("")) {
                    alertDialog("Please choose between AC or Non-AC");
                } else {
                    try {
                        Book();
                    }
                    catch (UnsupportedEncodingException e){
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
        String data = URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(strEmail, "UTF-8");
        data += "&" + URLEncoder.encode("pickup", "UTF-8")
                + "=" + URLEncoder.encode(strPickup, "UTF-8");
        data += "&" + URLEncoder.encode("destination", "UTF-8")
                + "=" + URLEncoder.encode(strDestination, "UTF-8");
        data += "&" + URLEncoder.encode("bookFor", "UTF-8")
                + "=" + URLEncoder.encode(strBookFor, "UTF-8");
        data += "&" + URLEncoder.encode("number", "UTF-8")
                + "=" + URLEncoder.encode(strNumber, "UTF-8");
        data += "&" + URLEncoder.encode("ac", "UTF-8")
                + "=" + URLEncoder.encode(strAC, "UTF-8");
        data += "&" + URLEncoder.encode("trip", "UTF-8")
                + "=" + URLEncoder.encode(strTrip, "UTF-8");
        try {
            String response;
            URL url= new URL("http://riantservices.com/App_Data/Book.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            response = slurp(conn.getInputStream());
            respond(response);
        }
        catch(Exception ex) {
            alertDialog("Error in Connection. Please try later.");
        }
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

    public void respond(String response)throws IOException{
        BufferedReader reader = new BufferedReader(new StringReader(response));
        int Status = Integer.parseInt(reader.readLine());
        //String User_name = reader.readLine();
        //String User_email = reader.readLine();
        //String Role_Id = reader.readLine();
        if(Status == 1){

            //Intent mainActivity = new Intent(this, MainActivity.class);
            //startActivity(mainActivity);
            //overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);

        }
        else{
            alertDialog("System error, please contact with administrator");
        }
    }
}
