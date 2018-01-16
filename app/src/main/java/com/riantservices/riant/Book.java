package com.riantservices.riant;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class BookElements{
    private String pickup, destination, dateTime, distance, driver, contact, fare;
    BookElements(String pickup, String destination, String dateTime, String distance, String driver, String contact, String fare) {
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

    String getDistance() {
        return distance;
    }

    String getDateTime() {
        return dateTime;
    }

    String getDriver() {
        return driver;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) { this.contact = contact;}

    public String getFare() { return fare;}
}

public class Book extends Fragment {

    private List<BookElements> BookList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        BookAdapter mAdapter = new BookAdapter(BookList);
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
        final SessionManager session = new SessionManager(getActivity());
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost("url");
                    json.put("email", session.getEmail());
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
        JSONObject resultArrayJson;
        JSONArray book = result.getJSONArray("history");
        String pickup,destination,dateTime,distance,driver,contact,fare;
        BookElements BookElement;
        int lenArray = book.length();
        for (int i = 0; i < lenArray; i++) {
            resultArrayJson = book.getJSONObject(i);
            pickup = resultArrayJson.getString("pickup");
            destination = resultArrayJson.getString("destination");
            dateTime = resultArrayJson.getString("dateTime");
            distance = result.getString("distance");
            driver = result.getString("driver");
            contact = result.getString("contact");
            fare = result.getString("fare");
            BookElement = new BookElements(pickup,destination,dateTime,distance,driver,contact,fare);
            BookList.add(BookElement);
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