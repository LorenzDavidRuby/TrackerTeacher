package com.example.trackerteacher

/**
 * Represents a Professor's record retrieved from the RDBMS.
 * Used to display availability based on predefined schedules.
 */
data class Item(
    val name: String,               // Professor Name
    val course: String,             // Department/Program
    val image: Int,                 // Resource ID for profile picture
    val scheduleImage: Int = R.drawable.ic_launcher_foreground // Schedule image resource (default placeholder)
)