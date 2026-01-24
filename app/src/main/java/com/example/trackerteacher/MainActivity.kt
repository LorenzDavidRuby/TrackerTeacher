package com.example.trackerteacher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // login button
        val loginButtonStudent = findViewById<Button>(R.id.BTN_studentloginbutton)
        val loginButtonProfessor = findViewById<Button>(R.id.BTN_facultyloginbutton)

        loginButtonStudent.setOnClickListener {
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
        }

        loginButtonProfessor.setOnClickListener {
            val intent = Intent(this, TeacherAccessActivity::class.java)
            startActivity(intent)
        }
    }
}
