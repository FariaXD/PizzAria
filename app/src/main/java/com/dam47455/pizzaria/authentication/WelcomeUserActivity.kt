package com.dam47455.pizzaria.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.dam47455.pizzaria.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WelcomeUserActivity : AppCompatActivity() {
    private lateinit var fAuth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_user)
        MainActivity.makeFullScreen(window)
        fAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance(MainActivity.databaseURL).getReference("Users")
        databaseReference.child(fAuth.uid!!).child("firstName").get().addOnSuccessListener {
            findViewById<TextView>(R.id.welcomeUsername).text = "Welcome, " + it.value.toString()
            findViewById<ConstraintLayout>(R.id.welcomeScreen).setOnClickListener {
                Toast.makeText(this, "Pressed to continue", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            findViewById<TextView>(R.id.welcomeUsername).text = "Failed to connect to database."
        }

        findViewById<Button>(R.id.welcomeLogout).setOnClickListener{
            val fAuth = FirebaseAuth.getInstance()
            fAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}