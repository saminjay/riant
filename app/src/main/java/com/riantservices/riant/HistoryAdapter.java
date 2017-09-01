package com.riantservices.riant;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<HistoryElements> HistoryList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView destination, dateTime, amount;
        private LatLng location;

        MyViewHolder(View view) {
            super(view);
            destination = (TextView) view.findViewById(R.id.destination);
            dateTime = (TextView) view.findViewById(R.id.dateTime);
            amount = (TextView) view.findViewById(R.id.amount);
            location=new LatLng(0,0);
        }
    }


    HistoryAdapter(List<HistoryElements> HistoryList) {
        this.HistoryList = HistoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryElements HistoryElements = HistoryList.get(position);
        holder.location=HistoryElements.getLatLng();
        //make map from getLatLng
        holder.destination.setText("Destination: "+HistoryElements.getDestination());
        holder.dateTime.setText(HistoryElements.getDateTime());
        holder.amount.setText("Rs. "+HistoryElements.getAmount());
    }

    @Override
    public int getItemCount() {
        return HistoryList.size();
    }
}
