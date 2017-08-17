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
    private String title,genre,year;
    public BookElements(String title, String genre, String year) {
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
                Toast.makeText(getActivity(), bookElements.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchBookData();
        return rootView;
    }
    private void fetchBookData() {
        BookElements bookElements = new BookElements("Mad Max: Fury Road", "Action & Adventure", "2015");
        BookList.add(bookElements);

        bookElements = new BookElements("Inside Out", "Animation, Kids & Family", "2015");
        BookList.add(bookElements);

        bookElements = new BookElements("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        BookList.add(bookElements);

        bookElements = new BookElements("Shaun the Sheep", "Animation", "2015");
        BookList.add(bookElements);

        bookElements = new BookElements("The Martian", "Science Fiction & Fantasy", "2015");
        BookList.add(bookElements);

        bookElements = new BookElements("Mission: Impossible Rogue Nation", "Action", "2015");
        BookList.add(bookElements);

        bookElements = new BookElements("Up", "Animation", "2009");
        BookList.add(bookElements);

        bookElements = new BookElements("Star Trek", "Science Fiction", "2009");
        BookList.add(bookElements);

        bookElements = new BookElements("The LEGO bookElements", "Animation", "2014");
        BookList.add(bookElements);

        bookElements = new BookElements("Iron Man", "Action & Adventure", "2008");
        BookList.add(bookElements);

        bookElements = new BookElements("Aliens", "Science Fiction", "1986");
        BookList.add(bookElements);

        bookElements = new BookElements("Chicken Run", "Animation", "2000");
        BookList.add(bookElements);

        bookElements = new BookElements("Back to the Future", "Science Fiction", "1985");
        BookList.add(bookElements);

        bookElements = new BookElements("Raiders of the Lost Ark", "Action & Adventure", "1981");
        BookList.add(bookElements);

        bookElements = new BookElements("Goldfinger", "Action & Adventure", "1965");
        BookList.add(bookElements);

        bookElements = new BookElements("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        BookList.add(bookElements);

        mAdapter.notifyDataSetChanged();
    }
}