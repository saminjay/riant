package com.riantservices.riant;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

class BookElements{
    private String pickup, destination, dateTime, distance, driver, contact, fare;
    public BookElements(String pickup, String destination, String dateTime, String distance, String driver, String contact, String fare) {
        this.pickup=pickup;
        this.destination=destination;
        this.dateTime=dateTime;
        this.distance=distance;
        this.driver=driver;
        this.contact=contact;
        this.fare=fare;
    }
    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) { this.contact = contact;}

    public String getFare() { return fare;}

    public void setFare(String fare) {
        this.fare = fare;
    }
}

public class Book extends Fragment {

    private List<BookElements> BookList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BookAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        mAdapter = new BookAdapter(BookList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookElements bookElements = BookList.get(position);
                Toast.makeText(getActivity(), bookElements.getPickup()+ " to "+bookElements.getDestination(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchBookData();
        return rootView;
    }
    private void fetchBookData() {
        SessionManager session = new SessionManager(getActivity());
        String Email=session.getEmail();
        try {
            String data = URLEncoder.encode("email", "UTF-8")
                    + "=" + URLEncoder.encode(Email, "UTF-8");
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
            String pickup = reader.readLine();
            String destination = reader.readLine();
            String dateTime = reader.readLine();
            String distance = reader.readLine();
            String driver = reader.readLine();
            String contact = reader.readLine();
            String fare = reader.readLine();
            BookElements BookElements = new BookElements(pickup,destination,dateTime,distance,driver,contact,fare);
            BookList.add(BookElements);
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