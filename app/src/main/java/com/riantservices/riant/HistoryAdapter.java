package com.riantservices.riant;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<HistoryElements> HistoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public HistoryAdapter(List<HistoryElements> HistoryList) {
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
        holder.title.setText(HistoryElements.getTitle());
        holder.genre.setText(HistoryElements.getGenre());
        holder.year.setText(HistoryElements.getYear());
    }

    @Override
    public int getItemCount() {
        return HistoryList.size();
    }
}
