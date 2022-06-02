package com.dam47455.pizzaria.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.dam47455.pizzaria.R
import com.google.firebase.auth.FirebaseAuth

class WelcomeUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_user)
        MainActivity.makeFullScreen(window)

        //val username = intent.extras!!.get("username")

        //findViewById<TextView>(R.id.welcomeUsername).text = "Welcome, " + username.toString()

        findViewById<ConstraintLayout>(R.id.welcomeScreen).setOnClickListener {
            Toast.makeText(this, "Pressed to continue", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.welcomeLogout).setOnClickListener{
            val fAuth = FirebaseAuth.getInstance()
            fAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}