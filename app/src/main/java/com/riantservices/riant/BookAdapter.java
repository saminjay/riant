package com.riantservices.riant;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private List<BookElements> bookList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pickup, destination, dateTime, distance, driver, contact, fare;

        public MyViewHolder(View view) {
            super(view);
            pickup = view.findViewById(R.id.pickup_addr);
            destination = view.findViewById(R.id.destination);
            dateTime = view.findViewById(R.id.dateTime);
            distance = view.findViewById(R.id.distance);
            driver = view.findViewById(R.id.driver);
            contact = view.findViewById(R.id.contact);
            fare = view.findViewById(R.id.fare);
        }
    }


    public BookAdapter(List<BookElements> bookList) {
        this.bookList = bookList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BookElements bookElements = bookList.get(position);
        holder.pickup.setText("Pickup Address: "+bookElements.getPickup());
        holder.destination.setText("Destination Address: "+bookElements.getDestination());
        holder.fare.setText("Estimated Fare: "+bookElements.getFare());
        holder.contact.setText("Contact: "+bookElements.getContact());
        holder.driver.setText("Driver's Name:"+bookElements.getDriver());
        holder.dateTime.setText(bookElements.getDateTime());
        holder.distance.setText("Distance: "+bookElements.getDistance());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
