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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get data from intent
        val facultyName = intent.getStringExtra("FACULTY_NAME") ?: "Unknown"
        val facultyCourse = intent.getStringExtra("FACULTY_COURSE") ?: "N/A"
        val facultyImage = intent.getIntExtra("FACULTY_IMAGE", R.drawable.ic_launcher_foreground)
        val scheduleImage = intent.getIntExtra("SCHEDULE_IMAGE", R.drawable.ic_launcher_foreground)

        // Setup UI elements
        val btnBack = findViewById<ImageButton>(R.id.BTN_back)
        val facultyNameText = findViewById<TextView>(R.id.TV_facultyName)
        val facultyCourseText = findViewById<TextView>(R.id.TV_facultyCourse)
        val profileImage = findViewById<ShapeableImageView>(R.id.IV_facultyProfile)
        val scheduleImageView = findViewById<ImageView>(R.id.IV_scheduleImage)

        // Set data
        facultyNameText.text = facultyName
        facultyCourseText.text = facultyCourse
        profileImage.setImageResource(facultyImage)
        scheduleImageView.setImageResource(scheduleImage)


        // Back button functionality
        btnBack.setOnClickListener {
            finish()
        }

        // Optional: Add pinch-to-zoom functionality for schedule image
        setupImageZoom(scheduleImageView)
    }

    /**
     * Setup basic zoom functionality for schedule image
     * For better zoom, consider using PhotoView library
     */
    private fun setupImageZoom(imageView: ImageView) {
        var scaleFactor = 1.0f

        imageView.setOnClickListener {
            // Toggle between normal and zoomed
            scaleFactor = if (scaleFactor == 1.0f) 2.0f else 1.0f
            imageView.animate()
                .scaleX(scaleFactor)
                .scaleY(scaleFactor)
                .setDuration(300)
                .start()
        }
    }
}