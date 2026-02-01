package com.example.trackerteacher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trackerteacher.databinding.ActivityStudentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class StudentRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentRegisterBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStudentRegisterBinding.inflate(layoutInflater)
        databaseHelper = DatabaseHelper(this)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.BTNStudentSignUp.setOnClickListener {
            val signupFullname = binding.ETStudentFullName.text.toString().trim()
            val signupEmail = binding.ETStudentEmail.text.toString().trim()
            val signupPassword = binding.ETStudentPassword.text.toString().trim()
            val signupProgram = binding.ETStudentCourse.text.toString().trim()

            if (signupFullname.isEmpty() || signupEmail.isEmpty() ||
                signupPassword.isEmpty() || signupProgram.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()

            } else {
                signUpDatabase(signupFullname, signupEmail, signupPassword, signupProgram)
            }
        }

        binding.TVLoginLink.setOnClickListener {
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun signUpDatabase(fullname: String,email:String,password:String,program:String) {
        val insertedRowId = databaseHelper.insertUser(fullname,email,password,program)
        if(insertedRowId != -1L) {
            Toast.makeText(this,"Signup Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this,"Signup Failed", Toast.LENGTH_SHORT).show()
        }
    }


}
