package com.riantservices.riant;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private List<BookElements> bookList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pickup, destination, dateTime, distance, driver, contact, fare;

        MyViewHolder(View view) {
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


    BookAdapter(List<BookElements> bookList) {
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
        holder.pickup.setText(String.format("Pickup Address: %s", bookElements.getPickup()));
        holder.destination.setText(String.format("Destination Address: %s", bookElements.getDestination()));
        holder.fare.setText(String.format("Estimated Fare: %s", bookElements.getFare()));
        holder.contact.setText(String.format("Contact: %s", bookElements.getContact()));
        holder.driver.setText(String.format("Driver's Name:%s", bookElements.getDriver()));
        holder.dateTime.setText(bookElements.getDateTime());
        holder.distance.setText(String.format("Distance: %s", bookElements.getDistance()));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
