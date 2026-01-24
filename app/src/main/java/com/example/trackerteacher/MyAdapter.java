package com.example.trackerteacher;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// ViewHolder class for RecyclerView items (itemview.xml)
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context; // Context of the activity
    List<Item> items; // List of items to display
    List<Item> itemsFull; // Original complete list

    public MyAdapter(Context context, List<Item> items) { // Constructor
        this.context = context;
        this.items = items;
        this.itemsFull = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.itemview, parent, false)); // Inflate the layout
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { // Bind data to the ViewHolder
        Item item = items.get(position); // Get the current item
        holder.nameView.setText(item.getName() + ", " + item.getCourse()); // Set the text of the views in the ViewHolder
        holder.statusRoomView.setText(item.getStatus() + " - " + item.getRoom()); // same
        holder.lastUpdateView.setText("Updated " + item.getLastUpdateOfStatus()); // same

        if (item.getStatus().equalsIgnoreCase("Available")) {
            holder.cardBackground.setBackgroundResource(R.drawable.card_background_available);
            holder.bellIcon.setColorFilter(Color.parseColor("#3498DB"));
        } else {
            holder.cardBackground.setBackgroundResource(R.drawable.card_background_offline);
            holder.bellIcon.setColorFilter(Color.parseColor("#7F8C8D"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // New generic search method
    public void search(String query) {
        String lowerCaseQuery = query.toLowerCase().trim();
        items.clear();
        if (lowerCaseQuery.isEmpty()) {
            items.addAll(itemsFull);
        } else {
            for (Item item : itemsFull) {
                if (item.getName().toLowerCase().contains(lowerCaseQuery) || 
                    item.getCourse().toLowerCase().contains(lowerCaseQuery)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Keep course filter for the button as well
    public void filterByCourse(String course) {
        items.clear();
        if (course.equals("All")) {
            items.addAll(itemsFull);
        } else {
            for (Item item : itemsFull) {
                if (item.getCourse().equalsIgnoreCase(course)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
