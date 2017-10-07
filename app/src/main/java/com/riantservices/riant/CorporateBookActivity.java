package com.riantservices.riant;

import android.content.DialogInterface;
import android.content.Intent;
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

import static com.riantservices.riant.R.color.colorBlack;
import static com.riantservices.riant.R.color.colorLight;
import static com.riantservices.riant.R.color.colorTransparent;
import static com.riantservices.riant.R.color.colorWhite;
import static com.riantservices.riant.R.drawable.buttonselected;
import static com.riantservices.riant.R.drawable.buttonshape;

public class CorporateBookActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Pickup, Destination, FriendContact, Time;
    private RadioButton radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9;
    private String strEmail, strBookFor, strCar, strAC, strPickup, strDestination, strNumber, strTime;
    private String mon,tue,wed,thu,fri,sat,sun;
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
        Button mon = (Button)findViewById(R.id.mon);
        Button tue = (Button)findViewById(R.id.tue);
        Button wed = (Button)findViewById(R.id.wed);
        Button thu = (Button)findViewById(R.id.thu);
        Button fri = (Button)findViewById(R.id.fri);
        Button sat = (Button)findViewById(R.id.sat);
        Button sun = (Button)findViewById(R.id.sun);
        Button button = (Button) findViewById(R.id.button);
        Button button1 = (Button) findViewById(R.id.button1);
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
        RadioGroup radio, radioA, radioB;
        Pickup = (EditText) findViewById(R.id.edit1);
        Destination = (EditText) findViewById(R.id.edit2);
        FriendContact = (EditText) findViewById(R.id.edit3);
        Time = (EditText)findViewById(R.id.time);
        radio = (RadioGroup) findViewById(R.id.radio);
        radioA = (RadioGroup) findViewById(R.id.radioA);
        radioB = (RadioGroup) findViewById(R.id.radioB);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);
        radio4 = (RadioButton) findViewById(R.id.radio4);
        radio5 = (RadioButton) findViewById(R.id.radio5);
        radio6 = (RadioButton) findViewById(R.id.radio6);
        radio7 = (RadioButton) findViewById(R.id.radio7);
        radio8 = (RadioButton) findViewById(R.id.radio8);
        radio9 = (RadioButton) findViewById(R.id.radio9);
        FriendContact.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        if (intent.hasExtra("Email")) {
            String Pickup_Location = intent.getStringExtra("pickupLocation");
            String Destination_Location = intent.getStringExtra("destinationLocation");
            Pickup.setText(Pickup_Location);
            Destination.setText(Destination_Location);
        }
        radio.clearCheck();
        radioA.clearCheck();
        radioB.clearCheck();

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
                        radio3.setBackgroundColor(getResources().getColor(colorLight));
                        radio4.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(colorTransparent));
                        break;
                    case R.id.radio4:
                        strCar = "";
                        strCar = "Sedan";
                        radio4.setBackgroundColor(getResources().getColor(colorLight));
                        radio3.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(colorTransparent));
                        break;
                    case R.id.radio5:
                        strCar = "";
                        strCar = "SUV";
                        radio5.setBackgroundColor(getResources().getColor(colorLight));
                        radio4.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio3.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(colorTransparent));
                        break;
                    case R.id.radio6:
                        strCar = "";
                        strCar = "Luxury";
                        radio6.setBackgroundColor(getResources().getColor(colorLight));
                        radio4.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio3.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio7.setBackgroundColor(getResources().getColor(colorTransparent));
                        break;
                    case R.id.radio7:
                        strCar = "";
                        strCar = "Limousine";
                        radio7.setBackgroundColor(getResources().getColor(colorLight));
                        radio4.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio5.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio6.setBackgroundColor(getResources().getColor(colorTransparent));
                        radio3.setBackgroundColor(getResources().getColor(colorTransparent));
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
                        radio8.setBackgroundColor(getResources().getColor(colorLight));
                        radio9.setBackgroundColor(getResources().getColor(colorTransparent));
                        break;
                    case R.id.radio9:
                        strAC = "";
                        strAC = "NonAC";
                        radio9.setBackgroundColor(getResources().getColor(colorLight));
                        radio8.setBackgroundColor(getResources().getColor(colorTransparent));
                        break;
                }
            }
        });

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TextView textView = (TextView) v;
                    textView.setBackgroundColor(getResources().getColor(colorWhite));
                    textView.setHintTextColor(getResources().getColor(colorBlack));
                    textView.setTextColor(getResources().getColor(colorBlack));
                } else {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mon:
                if(mon=="n") {view.setBackground(getResources().getDrawable(buttonselected));mon="y";}
                else {view.setBackground(getResources().getDrawable(buttonshape));mon="n";}
                break;
            case R.id.tue:
                if(tue=="n") {view.setBackground(getResources().getDrawable(buttonselected));tue="y";}
                else {view.setBackground(getResources().getDrawable(buttonshape));tue="n";}
                break;
            case R.id.wed:
                if(wed=="n") {view.setBackground(getResources().getDrawable(buttonselected));wed="y";}
                else {view.setBackground(getResources().getDrawable(buttonshape));wed="n";}
                break;
            case R.id.thu:
                if(thu=="n") {view.setBackground(getResources().getDrawable(buttonselected));thu="y";}
                else {view.setBackground(getResources().getDrawable(buttonshape));thu="n";}
                break;
            case R.id.fri:
                if(fri=="n") {view.setBackground(getResources().getDrawable(buttonselected));fri="y";}
                else {view.setBackground(getResources().getDrawable(buttonshape));fri="n";}
                break;
            case R.id.sat:
                if(sat=="n") {view.setBackground(getResources().getDrawable(buttonselected));sat="y";}
                else {view.setBackground(getResources().getDrawable(buttonshape));sat="n";}
                break;
            case R.id.sun:
                if(sun=="n") {view.setBackground(getResources().getDrawable(buttonselected));sun="y";}
                else {view.setBackground(getResources().getDrawable(buttonshape));sun="n";}
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
                } else if (mon=="n"&&tue=="n"&&wed=="n"&&thu=="n"&&fri=="n"&&sat=="n"&&sun=="n") {
                    alertDialog("Please select atleast one working day.");
                } else if (strCar.matches("")) {
                    alertDialog("Please choose a Car Type");
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
        data += "&" + URLEncoder.encode("mon", "UTF-8")
                + "=" + URLEncoder.encode(mon, "UTF-8");
        data += "&" + URLEncoder.encode("tue", "UTF-8")
                + "=" + URLEncoder.encode(tue, "UTF-8");
        data += "&" + URLEncoder.encode("wed", "UTF-8")
                + "=" + URLEncoder.encode(wed, "UTF-8");
        data += "&" + URLEncoder.encode("thu", "UTF-8")
                + "=" + URLEncoder.encode(thu, "UTF-8");
        data += "&" + URLEncoder.encode("fri", "UTF-8")
                + "=" + URLEncoder.encode(fri, "UTF-8");
        data += "&" + URLEncoder.encode("sat", "UTF-8")
                + "=" + URLEncoder.encode(sat, "UTF-8");
        data += "&" + URLEncoder.encode("sun", "UTF-8")
                + "=" + URLEncoder.encode(sun, "UTF-8");
        data += "&" + URLEncoder.encode("ac", "UTF-8")
                + "=" + URLEncoder.encode(strAC, "UTF-8");
        data += "&" + URLEncoder.encode("car", "UTF-8")
                + "=" + URLEncoder.encode(strCar, "UTF-8");
        try {
            String response;
            URL url = new URL("http://riantservices.com/App_Data/Book.php");
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

    public void respond(String response) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(response));
        int Status = Integer.parseInt(reader.readLine());
        //String User_name = reader.readLine();
        //String User_email = reader.readLine();
        //String Role_Id = reader.readLine();
        if (Status == 1) {

            //Intent mainActivity = new Intent(this, MainActivity.class);
            //startActivity(mainActivity);
            //overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);

        } else {
            alertDialog("System error, please contact with administrator");
        }
    }
}