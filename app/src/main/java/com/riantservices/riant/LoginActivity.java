package com.riantservices.riant;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.riantservices.riant.R.color.colorBlack;
import static com.riantservices.riant.R.color.colorTransparent;
import static com.riantservices.riant.R.color.colorWhite;

public class LoginActivity extends Activity implements OnClickListener  {

    private EditText EtLogEmail, EtLogPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnSignin, btnRegister;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        EtLogEmail = (EditText) findViewById(R.id.LogEmail);
        EtLogPass = (EditText) findViewById(R.id.LogPass);
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
        EtLogEmail.setOnFocusChangeListener(onFocusChangeListener);
        EtLogPass.setOnFocusChangeListener(onFocusChangeListener);
        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
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
                    try {
                        Login(strEmail, strPass);
                    }
                    catch (UnsupportedEncodingException e){
                        alertDialog("Unsupported Encoding");
                    }
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
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    protected void Login(final String Email,final String Password)throws UnsupportedEncodingException{
        String data = URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(Email, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8")
                + "=" + URLEncoder.encode(Password, "UTF-8");
        try {
            String response;
            URL url= new URL("http://riantservices.com/App_Data/Login.php");
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
        String User_name = reader.readLine();
        String User_email = reader.readLine();
        String Role_Id = reader.readLine();
        if(Status == 1){

            SessionManager session = new SessionManager(getApplicationContext());
            session.createLoginSession(User_name, User_email,Role_Id);
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);

        }else if(Status == 0){

            alertDialog("Wrong email and password");

        }else{
            alertDialog("System error, please contact with administrator");
        }
    }
}