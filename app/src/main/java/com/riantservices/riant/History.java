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

import com.google.android.gms.maps.model.LatLng;

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
        //HistoryElements HistoryElements = new HistoryElements();
        //HistoryList.add(HistoryElements);
        mAdapter.notifyDataSetChanged();
    }
}