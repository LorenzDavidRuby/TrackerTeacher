package com.example.trackerteacher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.imageview.ShapeableImageView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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
        tvFacultyCourse.text = if (facultyCourse.contains("Department")) {
            facultyCourse
        } else {
            "$facultyCourse Department"
        }

        ivProfile.setImageResource(facultyImage)
        ivSchedule.setImageResource(scheduleImage)

        // 4. Back button functionality
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 5. Setup Interactive Schedule View
        setupImageZoomAndPan(ivSchedule)
    }

    /**
     * Interactive Zoom & Pan logic: Tap to zoom, drag to pan when zoomed
     * with bounds constraint to prevent dragging beyond image edges
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupImageZoomAndPan(imageView: ImageView) {
        var isZoomed = false
        var lastTouchX = 0f
        var lastTouchY = 0f
        var posX = 0f
        var posY = 0f
        var isDragging = false
        val scaleFactor = 2.5f

        val gestureDetector = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    // Toggle zoom on tap
                    if (!isZoomed) {
                        // Zoom in
                        imageView.animate()
                            .scaleX(scaleFactor)
                            .scaleY(scaleFactor)
                            .setDuration(300)
                            .start()
                        isZoomed = true
                    } else {
                        // Zoom out and reset position
                        imageView.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .translationX(0f)
                            .translationY(0f)
                            .setDuration(300)
                            .start()
                        isZoomed = false
                        posX = 0f
                        posY = 0f
                    }
                    return true
                }
            })

        imageView.setOnTouchListener { view, event ->
            // Let gesture detector handle taps
            gestureDetector.onTouchEvent(event)

            // Handle dragging only when zoomed
            if (isZoomed) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastTouchX = event.rawX
                        lastTouchY = event.rawY
                        isDragging = false
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX - lastTouchX
                        val dy = event.rawY - lastTouchY

                        // Check if user is actually dragging (moved more than a threshold)
                        if (abs(dx) > 10 || abs(dy) > 10) {
                            isDragging = true
                        }

                        if (isDragging) {
                            posX += dx
                            posY += dy

                            // Calculate bounds to prevent dragging beyond image edges
                            val imageWidth = view.width.toFloat()
                            val imageHeight = view.height.toFloat()

                            // Calculate how much the image extends beyond the view when scaled
                            val scaledWidth = imageWidth * scaleFactor
                            val scaledHeight = imageHeight * scaleFactor

                            // Maximum translation is half the difference between scaled and original size
                            val maxTranslateX = (scaledWidth - imageWidth) / 2f
                            val maxTranslateY = (scaledHeight - imageHeight) / 2f

                            // Constrain position to keep image edges within bounds
                            posX = max(-maxTranslateX, min(posX, maxTranslateX))
                            posY = max(-maxTranslateY, min(posY, maxTranslateY))

                            // Apply translation
                            view.translationX = posX
                            view.translationY = posY

                            lastTouchX = event.rawX
                            lastTouchY = event.rawY
                        }
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        isDragging = false
                    }
                }
            }

            true
        }
    }
}