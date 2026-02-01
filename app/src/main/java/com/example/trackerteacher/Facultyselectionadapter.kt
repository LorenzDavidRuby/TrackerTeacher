package com.example.trackerteacher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class FacultySelectionAdapter(
    private val allFaculty: List<Item>,
    private val onFacultySelected: (Item) -> Unit
) : RecyclerView.Adapter<FacultySelectionAdapter.FacultyViewHolder>() {

    private var filteredFaculty: MutableList<Item> = allFaculty.toMutableList()
    var onItemSelected: ((Item) -> Unit)? = null

    class FacultyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.TV_facultyDialogName)
        val courseText: TextView = itemView.findViewById(R.id.TV_facultyDialogCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faculty_selection, parent, false)
        return FacultyViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
        val faculty = filteredFaculty[position]
        holder.nameText.text = faculty.name
        holder.courseText.text = faculty.course
        holder.itemView.setOnClickListener {
            onItemSelected?.invoke(faculty)
        }
    }

    override fun getItemCount(): Int = filteredFaculty.size

    /**
     * Filter the faculty list based on search query
     */
    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault()).trim()
        filteredFaculty.clear()

        if (lowerCaseQuery.isEmpty()) {
            filteredFaculty.addAll(allFaculty)
        } else {
            for (faculty in allFaculty) {
                if (faculty.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    faculty.course.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
                ) {
                    filteredFaculty.add(faculty)
                }
            }
        }
        notifyDataSetChanged()
    }
}