package com.riantservices.riant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;

public class TripMenu extends AppCompatActivity implements View.OnClickListener{

    Bundle Coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);
        Intent intent=getIntent();
        Coordinates=intent.getBundleExtra("Coordinates");
        int p=Array.getLength(Coordinates.getDoubleArray("lat"));
        Button button;
        if(p==2){
        button=findViewById(R.id.airport);
        button.setOnClickListener(this);
        button=findViewById(R.id.corporate);
        button.setOnClickListener(this);
        button=findViewById(R.id.local);
        button.setOnClickListener(this);
        button=findViewById(R.id.outstate);
        button.setOnClickListener(this);
        button=findViewById(R.id.outstation);
        button.setOnClickListener(this);
        button=findViewById(R.id.station);
        button.setOnClickListener(this);
        }
        else if(p>2){
            button=findViewById(R.id.airport);
            button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            button=findViewById(R.id.corporate);
            button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            button=findViewById(R.id.local);
            button.setOnClickListener(this);
            button=findViewById(R.id.outstate);
            button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            button=findViewById(R.id.outstation);
            button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            button=findViewById(R.id.station);
            button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.airport:
                intent=new Intent(v.getContext(),AirportBookActivity.class);
                intent.putExtras(Coordinates);
                startActivity(intent);
                break;
            case R.id.corporate:
                intent=new Intent(v.getContext(),CorporateBookActivity.class);
                intent.putExtras(Coordinates);
                startActivity(intent);
                break;
            case R.id.local:
                intent=new Intent(v.getContext(),LocalBookActivity.class);
                intent.putExtras(Coordinates);
                startActivity(intent);
                break;
            case R.id.outstate:
                intent=new Intent(v.getContext(),OutstateBookActivity.class);
                intent.putExtras(Coordinates);
                startActivity(intent);
                break;
            case R.id.outstation:intent=new Intent(v.getContext(),OutstateBookActivity.class);
                intent.putExtras(Coordinates);
                startActivity(intent);
                break;
            case R.id.station:
                intent=new Intent(v.getContext(),StationBookActivity.class);
                intent.putExtras(Coordinates);
                startActivity(intent);
                break;
        }
    }
}
