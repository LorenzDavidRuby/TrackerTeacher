package com.example.trackerteacher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
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

    // Define constants to ensure MyAdapter and this Activity stay in sync
    companion object {
        const val EXTRA_FACULTY_NAME = "FACULTY_NAME"
        const val EXTRA_FACULTY_COURSE = "FACULTY_COURSE"
        const val EXTRA_FACULTY_IMAGE = "FACULTY_IMAGE"
        const val EXTRA_SCHEDULE_IMAGE = "SCHEDULE_IMAGE"
        private const val ZOOM_SCALE = 2.5f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule)

        setupWindowInsets()
        bindFacultyData()

        findViewById<ImageButton>(R.id.BTN_back).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupImageZoomAndPan(findViewById(R.id.IV_scheduleImage))
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bindFacultyData() {
        // Extract data safely
        val name = intent.getStringExtra(EXTRA_FACULTY_NAME) ?: "Unknown Faculty"
        val course = intent.getStringExtra(EXTRA_FACULTY_COURSE) ?: "General"
        val profileRes = intent.getIntExtra(EXTRA_FACULTY_IMAGE, R.drawable.ic_launcher_foreground)
        val scheduleRes = intent.getIntExtra(EXTRA_SCHEDULE_IMAGE, R.drawable.ic_launcher_foreground)

        // Bind to UI
        findViewById<TextView>(R.id.TV_facultyName).text = name
        findViewById<TextView>(R.id.TV_facultyCourse).text = if (course.endsWith("Department")) {
            course
        } else {
            "$course Department"
        }

        findViewById<ShapeableImageView>(R.id.IV_facultyProfile).setImageResource(profileRes)
        findViewById<ImageView>(R.id.IV_scheduleImage).setImageResource(scheduleRes)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupImageZoomAndPan(imageView: ImageView) {
        var isZoomed = false
        var posX = 0f
        var posY = 0f
        var lastTouchX = 0f
        var lastTouchY = 0f

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                isZoomed = !isZoomed

                if (isZoomed) {
                    // Provide tactile feedback
                    imageView.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)

                    imageView.animate()
                        .scaleX(ZOOM_SCALE)
                        .scaleY(ZOOM_SCALE)
                        .setDuration(300)
                        .start()
                } else {
                    resetImage(imageView)
                    posX = 0f
                    posY = 0f
                }
                return true
            }
        })

        imageView.setOnTouchListener { view, event ->
            gestureDetector.onTouchEvent(event)

            if (isZoomed) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastTouchX = event.rawX
                        lastTouchY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX - lastTouchX
                        val dy = event.rawY - lastTouchY

                        posX += dx
                        posY += dy

                        // Constraints Calculation
                        val maxTX = (view.width * (ZOOM_SCALE - 1f)) / 2f
                        val maxTY = (view.height * (ZOOM_SCALE - 1f)) / 2f

                        posX = posX.coerceIn(-maxTX, maxTX)
                        posY = posY.coerceIn(-maxTY, maxTY)

                        view.translationX = posX
                        view.translationY = posY

                        lastTouchX = event.rawX
                        lastTouchY = event.rawY
                    }
                }
            }
            true
        }
    }

    private fun resetImage(view: View) {
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .translationX(0f)
            .translationY(0f)
            .setDuration(250)
            .start()
    }
}