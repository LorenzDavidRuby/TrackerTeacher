package com.example.trackerteacher

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentMenuPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_menu_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // list of teacher (item)
        val items = ArrayList<Item>()
        items.add(Item("Olivia Chen", "BSIT", R.drawable.ic_launcher_foreground, "just now", "Available", "Room 418"))
        items.add(Item("Adrian Vance", "BSIT", R.drawable.ic_launcher_foreground, "5 mins ago", "Available", "Lab 2"))
        items.add(Item("Dr. Elena Rodriguez", "BSCS", R.drawable.ic_launcher_foreground, "30 mins ago", "Busy", "Room 503"))
        items.add(Item("Prof. Willie Revillame", "BBA", R.drawable.ic_launcher_foreground, "23 hours", "Offline", "Home"))
        items.add(Item("Dr. Sigmund Freud", "BSPSY", R.drawable.ic_launcher_foreground, "Today", "Available", "Room 105"))
        items.add(Item("Prof. Carl Jung", "BSPSY", R.drawable.ic_launcher_foreground, "1 hour ago", "Busy", "Room 106"))
        items.add(Item("Marco Polo", "BSTOUR", R.drawable.ic_launcher_foreground, "Just now", "Available", "Travel Hub"))
        items.add(Item("Ferdinand Magellan", "BSTOUR", R.drawable.ic_launcher_foreground, "Yesterday", "Offline", "Port"))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview) // Replace with your RecyclerView ID
        recyclerView.layoutManager = LinearLayoutManager(this) // Set the layout manager

        val adapter = MyAdapter(this, items) // Replace with your adapter class and data
        recyclerView.adapter = adapter // Set the adapter


        // Setup Search Bar
        val etSearch = findViewById<EditText>(R.id.ET_searchProfessor)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.search(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Setup Filter Button
        val btnFilter = findViewById<Button>(R.id.BTN_filter)
        btnFilter.setOnClickListener {
            val popup = PopupMenu(this, btnFilter)
            popup.menu.add("All")
            popup.menu.add("BSIT")
            popup.menu.add("BSCS")
            popup.menu.add("BSPSY")
            popup.menu.add("BSTOUR")
            
            popup.setOnMenuItemClickListener { menuItem ->
                val selectedCourse = menuItem.title.toString()
                adapter.filterByCourse(selectedCourse)
                btnFilter.text = "Filter by Course: $selectedCourse"
                true
            }
            popup.show()
        }
    }
}
