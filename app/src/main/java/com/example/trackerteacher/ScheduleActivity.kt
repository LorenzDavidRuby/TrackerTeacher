package com.example.trackerteacher

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.imageview.ShapeableImageView

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule)

        // Handle edge-to-edge system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Get data passed from MyAdapter via Intent
        val facultyName = intent.getStringExtra("FACULTY_NAME") ?: "Unknown Faculty"
        val facultyCourse = intent.getStringExtra("FACULTY_COURSE") ?: "General"
        val facultyImage = intent.getIntExtra("FACULTY_IMAGE", R.drawable.ic_launcher_foreground)
        val scheduleImage = intent.getIntExtra("SCHEDULE_IMAGE", R.drawable.ic_launcher_foreground)

        // 2. Initialize UI elements based on your improved XML IDs
        val btnBack = findViewById<ImageButton>(R.id.BTN_back)
        val tvFacultyName = findViewById<TextView>(R.id.TV_facultyName)
        val tvFacultyCourse = findViewById<TextView>(R.id.TV_facultyCourse)
        val ivProfile = findViewById<ShapeableImageView>(R.id.IV_facultyProfile)
        val ivSchedule = findViewById<ImageView>(R.id.IV_scheduleImage)

        // 3. Bind data to views
        tvFacultyName.text = facultyName
        // Adding "Department" suffix to match your design intent
        tvFacultyCourse.text = if (facultyCourse.contains("Department")) facultyCourse else "$facultyCourse Department"

        ivProfile.setImageResource(facultyImage)
        ivSchedule.setImageResource(scheduleImage)

        // 4. Back button functionality
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Modern way to handle back navigation
        }

        // 5. Setup Interactive Schedule View
        setupImageZoom(ivSchedule)
    }

    /**
     * Interactive Zoom logic: Tap to enlarge the schedule image
     */
    private fun setupImageZoom(imageView: ImageView) {
        var isZoomed = false

        imageView.setOnClickListener {
            if (!isZoomed) {
                imageView.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .setDuration(300)
                    .start()
                isZoomed = true
            } else {
                imageView.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(300)
                    .start()
                isZoomed = false
            }
        }
    }
}