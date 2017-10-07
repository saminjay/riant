package com.riantservices.riant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TripMenu extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);
        Button button;
        button=(Button)findViewById(R.id.airport);
        button.setOnClickListener(this);
        button=(Button)findViewById(R.id.corporate);
        button.setOnClickListener(this);
        button=(Button)findViewById(R.id.local);
        button.setOnClickListener(this);
        button=(Button)findViewById(R.id.outstate);
        button.setOnClickListener(this);
        button=(Button)findViewById(R.id.outstation);
        button.setOnClickListener(this);
        button=(Button)findViewById(R.id.station);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.airport:
                intent=new Intent(v.getContext(),AirportBookActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                break;
            case R.id.corporate:
                intent=new Intent(v.getContext(),CorporateBookActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                break;
            case R.id.local:
                intent=new Intent(v.getContext(),LocalBookActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                break;
            case R.id.outstate:
                intent=new Intent(v.getContext(),OutstateBookActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                break;
            case R.id.outstation:intent=new Intent(v.getContext(),OutstateBookActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                break;
            case R.id.station:
                intent=new Intent(v.getContext(),StationBookActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                break;
        }
    }
}
