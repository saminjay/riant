package com.riantservices.riant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        //BookElements bookElements = new BookElements();
        //BookList.add(bookElements);

        mAdapter.notifyDataSetChanged();
    }
}