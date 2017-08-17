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

class HistoryElements{
    private String title,genre,year;
    public HistoryElements(String title, String genre, String year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
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
                Toast.makeText(getActivity(), HistoryElements.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchHistoryData();
        return rootView;
    }
    private void fetchHistoryData() {
        HistoryElements HistoryElements = new HistoryElements("Mad Max: Fury Road", "Action & Adventure", "2015");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Inside Out", "Animation, Kids & Family", "2015");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Shaun the Sheep", "Animation", "2015");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("The Martian", "Science Fiction & Fantasy", "2015");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Mission: Impossible Rogue Nation", "Action", "2015");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Up", "Animation", "2009");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Star Trek", "Science Fiction", "2009");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("The LEGO HistoryElements", "Animation", "2014");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Iron Man", "Action & Adventure", "2008");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Aliens", "Science Fiction", "1986");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Chicken Run", "Animation", "2000");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Back to the Future", "Science Fiction", "1985");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Raiders of the Lost Ark", "Action & Adventure", "1981");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Goldfinger", "Action & Adventure", "1965");
        HistoryList.add(HistoryElements);

        HistoryElements = new HistoryElements("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        HistoryList.add(HistoryElements);

        mAdapter.notifyDataSetChanged();
    }
}