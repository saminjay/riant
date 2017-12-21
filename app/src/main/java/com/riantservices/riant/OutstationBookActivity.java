package com.riantservices.riant;

import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class OutstationBookActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Pickup,Destination,FriendContact;
    private RadioButton radio2;
    private String strEmail,strBookFor,strTrip,strAC,strPickup, strDestination,strNumber;
    SessionManager session;
    ImageButton oneway,roundtrip;
    Button AC,NonAC;
    private LatLng pickup,destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strBookFor="";strTrip="";strAC="";strPickup="";strDestination="";strNumber="";
        setContentView(R.layout.activity_outstation_book);
        Button button=findViewById(R.id.button);
        Button button1=findViewById(R.id.button1);
        oneway=findViewById(R.id.oneway);
        roundtrip=findViewById(R.id.roundtrip);
        AC =findViewById(R.id.AC);
        NonAC=findViewById(R.id.NonAC);
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
        Pickup=findViewById(R.id.edit1);
        Destination=findViewById(R.id.edit2);
        FriendContact=findViewById(R.id.edit3);
        radio=findViewById(R.id.radio);
        radio2=findViewById(R.id.radio2);
        FriendContact.setVisibility(View.INVISIBLE);

        Bundle Coordinates=getIntent().getExtras();
        double[] lat=Coordinates.getDoubleArray("lat");
        double[] lng=Coordinates.getDoubleArray("lng");
        pickup=new LatLng(lat[0],lng[0]);
        destination=new LatLng(lat[1],lng[1]);
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
                    textView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    textView.setHintTextColor(getResources().getColor(R.color.colorBlack));
                    textView.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else{
                    TextView textView = (TextView) v;
                    textView.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
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
