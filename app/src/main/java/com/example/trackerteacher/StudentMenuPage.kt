package com.example.trackerteacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentMenuPage : AppCompatActivity() {
    private lateinit var items: ArrayList<Item>
    private lateinit var adapter: MyAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private var studentName: String = "Student"
    private var studentProgram: String = ""
    private var studentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_menu_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize database helper
        databaseHelper = DatabaseHelper(this)

        // Get student info from intent or SharedPreferences
        loadStudentInfo()

        // Set welcome message with student name
        updateWelcomeMessage()

        // Load faculty from database for this student
        items = databaseHelper.getFacultyForStudent(studentId)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the adapter with the empty list
        adapter = MyAdapter(this, items)
        recyclerView.adapter = adapter

        // Setup swipe-to-remove functionality
        setupSwipeToDelete(recyclerView)

        // Setup Add Faculty Button - Shows searchable dialog
        val btnAddFaculty = findViewById<Button>(R.id.BTN_addFaculty)
        btnAddFaculty.setOnClickListener {
            showAddFacultyDialog()
        }

        // Setup Logout Button
        val btnLogout = findViewById<ImageButton>(R.id.BTN_logout)
        btnLogout.setOnClickListener {
            logout()
        }

        updateEmptyState()
    }

    /**
     * All available faculty members that can be added
     * Note: Replace R.drawable.schedule_placeholder with actual schedule images
     * e.g., R.drawable.schedule_olivia_chen, R.drawable.schedule_adrian_vance, etc.
     */
    private val allFacultyMembers = listOf(
        Item("Lemuel Jun Bautista", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.lemueljunebautista),
        Item("Adrian Lee Magcalas", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.adrianleemagcalas),
        Item("Carl Joseph David", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.carljosephdavid),
        Item("Edward Allen Manaloto", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.edwardallenmanaloto),
        Item("Francisco Napalit", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.francisconapalit),
        Item("Harwin Mendoza", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.harwinmendoza),
        Item("Jerry Lucas", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.jerrylucas),
        Item("John Ray Medina", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.johnraymedina),
        Item("Lester Loyola", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.lesterloyola),
        Item("Mark Lagman", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.marklagman),
        Item("Michael John Endaya", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.michaeljohnendaya),
        Item("Ronielle Antonio", "BSIT", R.drawable.ic_launcher_foreground,R.drawable.ronielleantonio)
    )

    /**
     * Shows a searchable dialog with all available faculty members
     */
    private fun showAddFacultyDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_faculty, null)
        val searchEditText = dialogView.findViewById<EditText>(R.id.ET_searchFaculty)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.RV_facultyList)

        // Filter out already added faculty (check database)
        val availableFaculty = allFacultyMembers.filter { faculty ->
            !databaseHelper.isFacultyAddedForStudent(studentId, faculty.name)
        }.toMutableList()

        if (availableFaculty.isEmpty()) {
            Snackbar.make(
                findViewById(R.id.main),
                "All faculty members have been added",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        val dialogAdapter = FacultySelectionAdapter(availableFaculty) { selectedFaculty ->
            addFacultyMember(selectedFaculty)
        }
        recyclerView.adapter = dialogAdapter

        // Setup search functionality in dialog
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dialogAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Faculty Member")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()

        // Set click listener to dismiss dialog after selection
        dialogAdapter.onItemSelected = { selectedFaculty ->
            addFacultyMember(selectedFaculty)
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Adds a faculty member to the list (persists until removed)
     */
    private fun addFacultyMember(faculty: Item) {
        // Check if already added in database
        if (databaseHelper.isFacultyAddedForStudent(studentId, faculty.name)) {
            Snackbar.make(
                findViewById(R.id.main),
                "${faculty.name} is already in the list",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        // Save to database
        val result = databaseHelper.addFacultyForStudent(studentId, faculty)

        if (result != -1L) {
            // Add to local list
            items.add(faculty)
            adapter.notifyItemInserted(items.size - 1)
            adapter.updateFullList()
            updateEmptyState()

            // Scroll to the newly added item
            findViewById<RecyclerView>(R.id.recyclerview).smoothScrollToPosition(items.size - 1)

            Snackbar.make(
                findViewById(R.id.main),
                "${faculty.name} added",
                Snackbar.LENGTH_SHORT
            ).show()
        } else {
            Snackbar.make(
                findViewById(R.id.main),
                "Failed to add ${faculty.name}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Setup swipe-to-delete functionality
     */
    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedItem = items[position]

                // Remove from database
                databaseHelper.removeFacultyForStudent(studentId, deletedItem.name)

                // Remove from local list
                items.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.updateFullList()
                updateEmptyState()

                // Show snackbar with undo option
                Snackbar.make(
                    findViewById(R.id.main),
                    "${deletedItem.name} removed",
                    Snackbar.LENGTH_LONG
                ).setAction("UNDO") {
                    // Restore to database
                    databaseHelper.addFacultyForStudent(studentId, deletedItem)

                    // Restore to local list
                    items.add(position, deletedItem)
                    adapter.notifyItemInserted(position)
                    adapter.updateFullList()
                    updateEmptyState()
                    recyclerView.scrollToPosition(position)
                }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Shows/hides empty state message based on whether there are items
     */
    private fun updateEmptyState() {
        val emptyState = findViewById<TextView>(R.id.TV_emptyState)
        if (items.isEmpty()) {
            emptyState.visibility = TextView.VISIBLE
            findViewById<RecyclerView>(R.id.recyclerview).visibility = RecyclerView.GONE
        } else {
            emptyState.visibility = TextView.GONE
            findViewById<RecyclerView>(R.id.recyclerview).visibility = RecyclerView.VISIBLE
        }
    }

    /**
     * Load student information from intent or SharedPreferences
     */
    private fun loadStudentInfo() {
        // First try to get from intent
        studentName = intent.getStringExtra("STUDENT_NAME") ?: ""
        studentProgram = intent.getStringExtra("STUDENT_PROGRAM") ?: ""
        studentId = intent.getIntExtra("STUDENT_ID", -1)

        // If not in intent, try SharedPreferences
        if (studentName.isEmpty() || studentId == -1) {
            val sharedPreferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE)
            studentName = sharedPreferences.getString("student_name", "Student") ?: "Student"
            studentProgram = sharedPreferences.getString("student_program", "") ?: ""
            studentId = sharedPreferences.getInt("student_id", -1)
        }
    }

    /**
     * Update the welcome message with the logged-in student's name
     */
    private fun updateWelcomeMessage() {
        val welcomeHeader = findViewById<TextView>(R.id.TV_welcomeHeader)
        welcomeHeader.text = "Welcome Student,\n$studentName"
    }

    /**
     * Logout the current student and return to login screen
     */
    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Clear SharedPreferences
                val sharedPreferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                // Navigate back to login
                val intent = Intent(this, StudentLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}