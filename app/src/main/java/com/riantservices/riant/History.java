package com.riantservices.riant;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

class HistoryElements{
    private String destination,dateTime,amount;
    private LatLng latLng;
    public HistoryElements(String destination, String dateTime, String amount,double lat,double lng) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.amount = amount;
        latLng=new LatLng(lat,lng);
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LatLng getLatLng(){ return latLng;}

    public  void setLatLng(LatLng latLng){ this.latLng=latLng;}
}

public class History extends Fragment {

    private List<HistoryElements> HistoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        mAdapter = new HistoryAdapter(HistoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HistoryElements HistoryElements = HistoryList.get(position);
                Toast.makeText(getActivity(), HistoryElements.getDestination(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchHistoryData();
        return rootView;
    }
    private void fetchHistoryData() {
        SessionManager session = new SessionManager(getActivity());
        session.getEmail();
        try {
            String data = URLEncoder.encode("email", "UTF-8")
                    + "=" + URLEncoder.encode(session.getEmail(), "UTF-8");
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
        catch (UnsupportedEncodingException e){}
        mAdapter.notifyDataSetChanged();
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
        int n = Integer.parseInt(reader.readLine());
        for(int i=0;i<n;i++){
            String destination = reader.readLine();
            String dateTime = reader.readLine();
            String amt = reader.readLine();
            double lat = Double.parseDouble(reader.readLine());
            double lng = Double.parseDouble(reader.readLine());
            HistoryElements HistoryElements = new HistoryElements(destination,dateTime,amt,lat,lng);
            HistoryList.add(HistoryElements);
        }
    }

    public void alertDialog(String Message) {

        new AlertDialog.Builder(getActivity()).setTitle("Riant Alert").setMessage(Message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}