package com.dam47455.pizzaria.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.database.DatabaseRequest
import com.dam47455.pizzaria.data.User
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
        databaseReference = FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Users")
        databaseReference.child(fAuth.uid!!).get().addOnSuccessListener {
            val dataUser = User()
            dataUser.processDataSnapshot(it)
            findViewById<TextView>(R.id.welcomeUsername).text = getString(R.string.welcome_user_name, dataUser.firstName)
            findViewById<ConstraintLayout>(R.id.welcomeScreen).setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("User", dataUser)
                startActivity(intent)
            }
        }.addOnFailureListener {
            findViewById<TextView>(R.id.welcomeUsername).text = getString(R.string.failed_connection_db)
        }

        findViewById<Button>(R.id.welcomeLogout).setOnClickListener{
            val fAuth = FirebaseAuth.getInstance()
            fAuth.signOut()
            startActivity(Intent(this, FirstActivity::class.java))
        }
    }
}