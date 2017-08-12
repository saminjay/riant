package com.riantservices.riant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.riantservices.riant.R.color.colorBlack;
import static com.riantservices.riant.R.color.colorTransparent;
import static com.riantservices.riant.R.color.colorWhite;

public class RegisterActivity extends Activity implements OnClickListener {
    EditText UserName, Email, Phone, Pass1, Pass2;
    String strName, strEmail, strPhone,strPass, strPass2;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnRegister:
                strName = UserName.getText().toString();
                strEmail = Email.getText().toString();
                strPhone = Phone.getText().toString();
                strPass = Pass1.getText().toString();
                strPass2 = Pass2.getText().toString();
                if (strName.matches("")) {
                    alertDialog("Username is required");
                    break;
                }else if(strEmail.matches("")) {
                    alertDialog("Email is required");
                    break;
                }else if(strPhone.matches("")) {
                    alertDialog("Phone is required");
                    break;
                }else if(strPass.matches("")) {
                    alertDialog("Pasword is required");
                    break;
                }
                else if(!isValidEmailAddress(strEmail)) {
                    alertDialog("Introduce a valid email format");
                    break;
                }
                else if(!strPass.equals(strPass2)) {
                    alertDialog("Password not match");
                    break;
                }
                else{
                    try{
                        Register(strName, strEmail, strPhone, strPass);
                    }
                    catch (UnsupportedEncodingException e) {
                        alertDialog("Unsupported encoding");}
                }
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UserName = (EditText)findViewById(R.id.RegUsername);
        Email = (EditText)findViewById(R.id.RegEmail);
        Phone = (EditText)findViewById(R.id.RegPhone);
        Pass1 = (EditText)findViewById(R.id.RegPass);
        Pass2 = (EditText)findViewById(R.id.RegPass2);
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    protected void Register(final String Name, final String Email,final String Phone,final String Password)throws UnsupportedEncodingException {
        String response;
        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(Name, "UTF-8");
        data += "&" + URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(Email, "UTF-8");
        data += "&" + URLEncoder.encode("phone", "UTF-8")
                + "=" + URLEncoder.encode(Phone, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8")
                + "=" + URLEncoder.encode(Password, "UTF-8");
        try {
            java.net.URL url= new URL("http://riantservices.com/App_Data/Register.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            response = slurp(conn.getInputStream());
            respond(Integer.parseInt(response));
        }
        catch(Exception ex) {
            alertDialog("Error in Connection. Please try later.");
        }
    }

    @NonNull
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

    public void respond(int callBack){
        if(callBack == 1){
            final Intent GoLogin = new Intent(this, LoginActivity.class);

            new AlertDialog.Builder(this)
                    .setTitle("Riant Message")
                    .setMessage("You have successfully registered, Please login and enjoy our services")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            GoLogin.putExtra("Email", strEmail);
                            GoLogin.putExtra("Pass", strEmail);
                            startActivity(GoLogin);
                            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);

                        }
                    })
			        .setIcon(android.R.drawable.ic_input_add)
                    .show();

        }else if(callBack == 0){
            alertDialog("Registration failed, please try again");
        }else if(callBack == 2){
            alertDialog("Already exist a user with same email ID");
        }else{
            alertDialog("System error, please contact with administrator");
        }
    }
}