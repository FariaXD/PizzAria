package com.dam47455.pizzaria.authentication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R

import com.google.firebase.auth.FirebaseAuth

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        MainActivity.makeFullScreen(window)
        val fAuth = FirebaseAuth.getInstance()
        //fAuth.signOut()
        val user = fAuth.currentUser


        if(user != null){
            val intent = Intent(this, WelcomeUserActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.loginBtn).setOnClickListener {
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.registerBtn).setOnClickListener{
            val intent = Intent(this, UserRegisterActivity::class.java)
            startActivity(intent)
        }
    }
}