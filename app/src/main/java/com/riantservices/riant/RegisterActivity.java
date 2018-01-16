package com.riantservices.riant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class RegisterActivity extends Activity{
    EditText UserName, Email, Phone, Pass1, Pass2;
    Button BtnRegister;
    String strName, strEmail, strPhone,strPass, strPass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UserName = findViewById(R.id.RegUsername);
        Email = findViewById(R.id.RegEmail);
        Phone = findViewById(R.id.RegPhone);
        Pass1 = findViewById(R.id.RegPass);
        Pass2 = findViewById(R.id.RegPass2);
        BtnRegister = findViewById(R.id.BtnRegister);
        BtnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = UserName.getText().toString();
                strEmail = Email.getText().toString();
                strPhone = Phone.getText().toString();
                strPass = Pass1.getText().toString();
                strPass2 = Pass2.getText().toString();
                if (strName.matches("")) {
                    alertDialog("Username is required");
                }else if(strEmail.matches("")) {
                    alertDialog("Email is required");
                }else if(strPhone.matches("")) {
                    alertDialog("Phone is required");
                }else if(strPass.matches("")) {
                    alertDialog("Pasword is required");
                }
                else if(!isValidEmailAddress(strEmail)) {
                    alertDialog("Introduce a valid email format");
                }
                else if(!strPass.equals(strPass2)) {
                    alertDialog("Password not match");
                }
                else{
                    try{
                        Register(strName, strEmail, strPhone, strPass);
                    }
                    catch (UnsupportedEncodingException e) {
                        alertDialog("Unsupported encoding");}
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
        UserName.setOnFocusChangeListener(onFocusChangeListener);
        Email.setOnFocusChangeListener(onFocusChangeListener);
        Phone.setOnFocusChangeListener(onFocusChangeListener);
        Pass1.setOnFocusChangeListener(onFocusChangeListener);
        Pass2.setOnFocusChangeListener(onFocusChangeListener);

    }
    public void alertDialog(String Message){
        new AlertDialog.Builder(this)
                .setTitle("Riant Message")
                .setMessage(Message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert).show();

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    protected void Register(final String Name, final String Email,final String Phone,final String Password)throws UnsupportedEncodingException {
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost("url");
                    json.put("name", Name);
                    json.put("email", Email);
                    json.put("phone", Phone);
                    json.put("password", Password);
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
        JSONObject result = new JSONObject(in.toString());
        int Status = result.getInt("status");
        if(Status == 1){
            alertDialog("Registration Successful. Welcome to Riant Family.");
            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.putExtra("Email",strEmail);
            loginActivity.putExtra("Pass",strPass);
            startActivity(loginActivity);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);

        }else if(Status == 0){

            alertDialog("Email is already being used by another user.");

        }else{
            alertDialog("System error, please contact with administrator");
        }
    }
}