package com.example.trackerteacher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StudentLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLogin = findViewById<Button>(R.id.BTN_studentSubmitLogin)
        val btnCreateAccount = findViewById<Button>(R.id.BTN_studentCreateAccount)

        btnLogin.setOnClickListener {
            // Logic for authentication should go here
            val intent = Intent(this, StudentMenuPage::class.java)
            startActivity(intent)
        }

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, StudentRegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
