package com.example.trackerteacher

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameView: TextView = itemView.findViewById(R.id.TV_facultyName)
    val bellIcon: ImageView = itemView.findViewById(R.id.IV_notificationBell)
    val cardBackground: RelativeLayout = itemView.findViewById(R.id.RL_cardBackground)
}