package com.example.trackerteacher;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView nameView;
    TextView statusRoomView;
    TextView lastUpdateView;
    ImageView bellIcon;
    RelativeLayout cardBackground;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.TV_facultyName);
        statusRoomView = itemView.findViewById(R.id.TV_facultyStatusRoom);
        lastUpdateView = itemView.findViewById(R.id.TV_lastUpdated);
        bellIcon = itemView.findViewById(R.id.IV_notificationBell);
        cardBackground = itemView.findViewById(R.id.RL_cardBackground);
    }
}
