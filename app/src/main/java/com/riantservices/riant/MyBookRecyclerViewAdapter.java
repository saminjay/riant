package com.riantservices.riant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

class MyBookRecyclerViewAdapter extends RecyclerView.Adapter<MyBookRecyclerViewAdapter.ViewHolder> {

    private static final List<String> pickupContent = new ArrayList<>(20);
    private static final List<String> destinationContent = new ArrayList<>(20);
    private static final List<String> driverContent = new ArrayList<>(20);
    private static final List<String> contactContent = new ArrayList<>(20);
    private static final List<String> distanceContent = new ArrayList<>(20);
    private static final List<String> efareContent = new ArrayList<>(20);
    private static final List<String> date = new ArrayList<>(20);
    private int lenArray=0;

    MyBookRecyclerViewAdapter(String Email)throws Exception {
        String response;
        String data =  URLEncoder.encode("UserEmail", "UTF-8")
                + "=" + URLEncoder.encode(Email, "UTF-8");
        java.net.URL url = new URL("http://riantservices.com/App_Data/updateGPS.php");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        response = slurp(conn.getInputStream());
        JSONObject reader = new JSONObject(response);
        JSONObject StatusRequest = reader.getJSONObject("Response");
        JSONArray BookingsArray = StatusRequest.getJSONArray("Data");
        lenArray = BookingsArray.length();
        for (int i = 0; i < lenArray && i<20; i++) {
            JSONObject BookingArrayJson = BookingsArray.getJSONObject(i);
            pickupContent.add(BookingArrayJson.getString("pickup"));
            destinationContent.add(BookingArrayJson.getString("destination"));
            driverContent.add(BookingArrayJson.getString("driver"));
            contactContent.add(BookingArrayJson.getString("contact"));
            distanceContent.add(BookingArrayJson.getString("distance"));
            efareContent.add(BookingArrayJson.getString("efare"));
            date.add(BookingArrayJson.getString("date"));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pickupContent.setText(pickupContent.get(position));
        holder.destinationContent.setText(destinationContent.get(position));
        holder.driverContent.setText(driverContent.get(position));
        holder.distanceContent.setText(distanceContent.get(position));
        holder.contactContent.setText(contactContent.get(position));
        holder.efareContent.setText(efareContent.get(position));
        holder.date.setText(date.get(position));
    }

    @Override
    public int getItemCount() {
        return lenArray;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView pickupContent;
        final TextView destinationContent;
        final TextView driverContent;
        final TextView contactContent;
        final TextView distanceContent;
        final TextView efareContent;
        final TextView date;

         ViewHolder(View view) {
            super(view);
            pickupContent = (TextView) view.findViewById(R.id.pickupContent);
            destinationContent = (TextView) view.findViewById(R.id.destinationContent);
            driverContent = (TextView) view.findViewById(R.id.driverContent);
            contactContent = (TextView) view.findViewById(R.id.contactContent);
            distanceContent = (TextView) view.findViewById(R.id.distanceContent);
            efareContent = (TextView) view.findViewById(R.id.efareContent);
            date = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + "'" + pickupContent.getText() + "',+"+ destinationContent.getText() + "',"
                    + driverContent.getText() + "',"+ contactContent.getText() + "',";
        }
    }

    private static String slurp(final InputStream is) throws IOException {
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
}