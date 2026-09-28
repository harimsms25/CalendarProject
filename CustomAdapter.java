package com.example.calendarproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private ArrayList<String> events;

    public CustomAdapter(ArrayList<String> e){
        events = e;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //the the layout is inflated and gives the looks to each row
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //This is were the recycled holders have their views set. i.e., the holder at the top gets scrapped and is put back at the bottom
        //once that holder appears on screen, we set the text view of that holder to the event name at a position same as the holder in the list
        //Then we access the holder to get the text view and set it to the name of the event at the appropriate position

        String eventName = events.get(position);
        holder.textView.setText(eventName);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        //grabbing the views from the recycler layout xml and setting them equal to variables
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.event);
        }
    }
}
