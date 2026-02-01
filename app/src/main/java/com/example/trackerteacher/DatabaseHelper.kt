package com.example.trackerteacher

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 5  // Incremented version

        // Users table
        private const val TABLE_USERS = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FULLNAME = "fullname"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_PROGRAM = "program"

        // Student-Faculty tracking table
        private const val TABLE_STUDENT_FACULTY = "student_faculty"
        private const val COLUMN_SF_ID = "id"
        private const val COLUMN_STUDENT_ID = "student_id"
        private const val COLUMN_FACULTY_NAME = "faculty_name"
        private const val COLUMN_FACULTY_COURSE = "faculty_course"
        private const val COLUMN_FACULTY_IMAGE = "faculty_image"
        private const val COLUMN_FACULTY_SCHEDULE_IMAGE = "faculty_schedule_image"
        private const val COLUMN_ADDED_DATE = "added_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create users table
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_FULLNAME TEXT, " +
                "$COLUMN_EMAIL TEXT UNIQUE," +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_PROGRAM TEXT)")
        db?.execSQL(createUsersTable)

        // Create student-faculty tracking table
        val createStudentFacultyTable = ("CREATE TABLE $TABLE_STUDENT_FACULTY (" +
                "$COLUMN_SF_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_STUDENT_ID INTEGER, " +
                "$COLUMN_FACULTY_NAME TEXT, " +
                "$COLUMN_FACULTY_COURSE TEXT, " +
                "$COLUMN_FACULTY_IMAGE INTEGER, " +
                "$COLUMN_FACULTY_SCHEDULE_IMAGE INTEGER, " +
                "$COLUMN_ADDED_DATE TEXT, " +
                "FOREIGN KEY($COLUMN_STUDENT_ID) REFERENCES $TABLE_USERS($COLUMN_ID))")
        db?.execSQL(createStudentFacultyTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT_FACULTY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun insertUser(fullname: String, email: String, password: String, program: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_FULLNAME, fullname)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_PROGRAM, program)
        }
        val db = writableDatabase
        return db.insert(TABLE_USERS, null, values)
    }

    fun readUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)

        val userExist = cursor.count > 0
        cursor.close()
        return userExist
    }

    /**
     * Get user details by email and password
     * Returns a Student object if found, null otherwise
     */
    fun getUserDetails(email: String, password: String): Student? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor: Cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)

        var student: Student? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val fullname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val userPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            val program = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM))

            student = Student(id, fullname, userEmail, userPassword, program)
        }
        cursor.close()
        return student
    }

    /**
     * Add a faculty member to a student's tracked list
     */
    fun addFacultyForStudent(studentId: Int, faculty: Item): Long {
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_ID, studentId)
            put(COLUMN_FACULTY_NAME, faculty.name)
            put(COLUMN_FACULTY_COURSE, faculty.course)
            put(COLUMN_FACULTY_IMAGE, faculty.image)
            put(COLUMN_FACULTY_SCHEDULE_IMAGE, faculty.scheduleImage)
            put(COLUMN_ADDED_DATE, System.currentTimeMillis().toString())
        }
        val db = writableDatabase
        return db.insert(TABLE_STUDENT_FACULTY, null, values)
    }

    /**
     * Get all faculty members for a specific student
     */
    fun getFacultyForStudent(studentId: Int): ArrayList<Item> {
        val facultyList = ArrayList<Item>()
        val db = readableDatabase
        val selection = "$COLUMN_STUDENT_ID = ?"
        val selectionArgs = arrayOf(studentId.toString())

        val cursor: Cursor = db.query(
            TABLE_STUDENT_FACULTY,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COLUMN_ADDED_DATE ASC"  // Order by date added
        )

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACULTY_NAME))
                val course = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACULTY_COURSE))
                val image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FACULTY_IMAGE))
                val scheduleImage = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FACULTY_SCHEDULE_IMAGE))

                facultyList.add(Item(name, course, image, scheduleImage))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return facultyList
    }

    /**
     * Remove a faculty member from a student's tracked list
     */
    fun removeFacultyForStudent(studentId: Int, facultyName: String): Int {
        val db = writableDatabase
        val selection = "$COLUMN_STUDENT_ID = ? AND $COLUMN_FACULTY_NAME = ?"
        val selectionArgs = arrayOf(studentId.toString(), facultyName)
        return db.delete(TABLE_STUDENT_FACULTY, selection, selectionArgs)
    }

    /**
     * Check if a student has already added a specific faculty member
     */
    fun isFacultyAddedForStudent(studentId: Int, facultyName: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_STUDENT_ID = ? AND $COLUMN_FACULTY_NAME = ?"
        val selectionArgs = arrayOf(studentId.toString(), facultyName)
        val cursor = db.query(TABLE_STUDENT_FACULTY, null, selection, selectionArgs, null, null, null)

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    /**
     * Clear all faculty members for a student
     */
    fun clearAllFacultyForStudent(studentId: Int): Int {
        val db = writableDatabase
        val selection = "$COLUMN_STUDENT_ID = ?"
        val selectionArgs = arrayOf(studentId.toString())
        return db.delete(TABLE_STUDENT_FACULTY, selection, selectionArgs)
    }
}

/**
 * Data class to hold student information
 */
data class Student(
    val id: Int,
    val fullname: String,
    val email: String,
    val password: String,
    val program: String
)