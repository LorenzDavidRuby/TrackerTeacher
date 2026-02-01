package com.example.trackerteacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trackerteacher.databinding.ActivityStudentLoginBinding

class StudentLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentLoginBinding.inflate(layoutInflater)
        databaseHelper = DatabaseHelper(this)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.BTNStudentSubmitLogin.setOnClickListener {
            val loginEmail = binding.ETStudentEmail.text.toString()
            val loginPassword = binding.ETStudentPassword.text.toString()
            loginDatabase(loginEmail, loginPassword)
        }

        binding.BTNStudentCreateAccount.setOnClickListener {
            val intent = Intent(this, StudentRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginDatabase(email: String, password: String) {
        val userExist = databaseHelper.readUser(email, password)
        if (userExist) {
            // Get full user details
            val student = databaseHelper.getUserDetails(email, password)

            if (student != null) {
                // Save student info to SharedPreferences for persistence
                saveStudentToPreferences(student)

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                // Start StudentMenuPage with student info
                val intent = Intent(this, StudentMenuPage::class.java).apply {
                    putExtra("STUDENT_NAME", student.fullname)
                    putExtra("STUDENT_EMAIL", student.email)
                    putExtra("STUDENT_PROGRAM", student.program)
                    putExtra("STUDENT_ID", student.id)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error retrieving user data", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Save student information to SharedPreferences for later access
     */
    private fun saveStudentToPreferences(student: Student) {
        val sharedPreferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("student_name", student.fullname)
        editor.putString("student_email", student.email)
        editor.putString("student_program", student.program)
        editor.putInt("student_id", student.id)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }
}