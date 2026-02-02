package com.example.trackerteacher

import android.content.Context
import android.content.Intent
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

        // 1. Set text views separately to match the new design
        holder.nameView.text = item.name

        // Formats the department text nicely
        holder.courseView.text = if (item.course.contains("Department")) {
            item.course
        } else {
            "${item.course} Department"
        }

        // 2. Set the Profile Image (Matching the new IV_facultyProfile ID)
        holder.profileImageView.setImageResource(item.image)

        // 3. Navigation logic to ScheduleActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ScheduleActivity::class.java).apply {
                // Passing data to the next activity
                putExtra("FACULTY_NAME", item.name)
                putExtra("FACULTY_COURSE", item.course)
                putExtra("FACULTY_IMAGE", item.image)
                putExtra("SCHEDULE_IMAGE", item.scheduleImage)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    // --- Search and Filter Logic ---

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

    fun updateFullList() {
        itemsFull.clear()
        itemsFull.addAll(items)
    }
}