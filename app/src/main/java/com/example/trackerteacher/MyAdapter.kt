package com.example.trackerteacher

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MyAdapter(private val context: Context, private var items: MutableList<Item>) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val itemsFull: MutableList<Item> = ArrayList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        // Displays Professor Name and Department
        holder.nameView.text = "${item.name}, ${item.course}"


        // Make the entire card clickable to view schedule
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ScheduleActivity::class.java).apply {
                putExtra("FACULTY_NAME", item.name)
                putExtra("FACULTY_COURSE", item.course)
                putExtra("FACULTY_IMAGE", item.image)
                putExtra("SCHEDULE_IMAGE", item.scheduleImage) // Schedule image resource
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * Search logic filtering by Professor Name or Department
     */
    fun search(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault()).trim()
        items.clear()
        if (lowerCaseQuery.isEmpty()) {
            items.addAll(itemsFull)
        } else {
            for (item in itemsFull) {
                if (item.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    item.course.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
                ) {
                    items.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    /**
     * Filter specifically by Department/Program
     */
    fun filterByCourse(course: String) {
        items.clear()
        if (course == "All") {
            items.addAll(itemsFull)
        } else {
            for (item in itemsFull) {
                if (item.course.equals(course, ignoreCase = true)) {
                    items.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    /**
     * Update the full list when items are added or removed
     */
    fun updateFullList() {
        itemsFull.clear()
        itemsFull.addAll(items)
    }
}