package com.riantservices.riant.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity implements OnClickListener  {

    private EditText EtLogEmail, EtLogPass;
    static int rslt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnSignin, btnRegister;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        EtLogEmail = findViewById(R.id.LogEmail);
        EtLogPass = findViewById(R.id.LogPass);
        if(intent.hasExtra("Email")) {
            String Email_User_From_register = intent.getStringExtra("Email");
            String PassUser_From_register = intent.getStringExtra("Pass");
            EtLogEmail.setText(Email_User_From_register);
            EtLogPass.setText(PassUser_From_register);
        }
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
        EtLogEmail.setOnFocusChangeListener(onFocusChangeListener);
        EtLogPass.setOnFocusChangeListener(onFocusChangeListener);
        btnSignin = findViewById(R.id.btnSignin);
        btnRegister = findViewById(R.id.btnRegister);
        btnSignin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        EtLogPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    public void onClick(View view) {
        String strEmail, strPass;
        switch (view.getId()) {
            case R.id.btnSignin:
                strEmail = EtLogEmail.getText().toString();
                strPass = EtLogPass.getText().toString();
                if (strEmail.matches("")) {
                    alertDialog("Email is required");
                    break;
                } else if (strPass.matches("")) {
                    alertDialog("Password is required");
                    break;
                } else if (!isValidEmailAddress(strEmail)) {
                    alertDialog("Invalid Email Id");
                    break;
                } else {
                    Login(strEmail, strPass);
                }
                break;
            case R.id.btnRegister:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivity(register);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    protected void Login(final String email, final String pwd) {
        try {
            rslt=0;
            CallerchkUser c= new CallerchkUser();
            c.a=email;
            c.b=pwd;
            c.join();
            c.start();
            //while(rslt==0){
                try{
                    Thread.sleep(1000);
                }catch(Exception ignored) {}
            //}

            if(rslt == 1){
                alertDialog("Login Succcessful");
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
            else
            {
                alertDialog("Invalid Username or Password, Try again!!");
            }

        } catch(Exception e) {
            e.printStackTrace();
            alertDialog("Error: Cannot Estabilish Connection");
        }
    }
}