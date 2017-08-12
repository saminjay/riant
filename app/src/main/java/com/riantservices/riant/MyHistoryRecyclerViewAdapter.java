package com.riantservices.riant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

class MyHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ViewHolder> {

    private static final List<String> destinationContent = new ArrayList<>(20);
    private static final List<String> car = new ArrayList<>(20);
    private static final List<String> lat = new ArrayList<>(20);
    private static final List<String> lng = new ArrayList<>(20);
    private static final List<String> efareContent = new ArrayList<>(20);
    private static final List<String> date = new ArrayList<>(20);
    private int lenArray=0;

    MyHistoryRecyclerViewAdapter(String Email)throws Exception {
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
            destinationContent.add(BookingArrayJson.getString("destination"));
            lat.add(BookingArrayJson.getString("lat"));
            lng.add(BookingArrayJson.getString("lng"));
            car.add(BookingArrayJson.getString("car"));
            efareContent.add(BookingArrayJson.getString("efare"));
            date.add(BookingArrayJson.getString("date"));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|"
                + lat.get(position)
                + ","
                + lng.get(position)
                + "&sensor=false&key=AIzaSyCyIU87s-ta65mq4hd-yju8PUyTxxsAsZU";
        new DownloadImageTask(holder.map).execute(getMapURL);
        holder.destinationContent.setText(destinationContent.get(position));
        holder.car.setText(car.get(position));
        holder.date.setText(date.get(position));
        holder.efareContent.setText(efareContent.get(position));
    }

    @Override
    public int getItemCount() {
        return lenArray;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView destinationContent;
        final TextView car;
        final ImageView map;
        final TextView efareContent;
        final TextView date;

        ViewHolder(View view) {
            super(view);
            car = (TextView) view.findViewById(R.id.car);
            destinationContent = (TextView) view.findViewById(R.id.destinationContent);
            map = (ImageView) view.findViewById(R.id.map);
            efareContent = (TextView) view.findViewById(R.id.efareContent);
            date = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() +  destinationContent.getText() + "',"
                    + car.getText() + "',"+ efareContent.getText() + "',"+ date.getText();
        }
    }

    @NonNull
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}