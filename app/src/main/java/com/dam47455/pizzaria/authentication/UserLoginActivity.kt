package com.dam47455.pizzaria.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.dam47455.pizzaria.R
import com.google.firebase.auth.FirebaseAuth

class UserLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        MainActivity.makeFullScreen(window)
        val fAuth : FirebaseAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.loginRegisterBtn).setOnClickListener{
            var intent = Intent(this, UserRegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.loginSubmitBtn).setOnClickListener{
            val username = findViewById<EditText>(R.id.loginEmail)
            val password = findViewById<EditText>(R.id.loginPassword)
            val loginUser = LoginUser(username.text.toString(), password.text.toString())
            when(loginUser.validateLoginFields()){
                LoginUser.LoginValidation.NOERRORS ->{
                    fAuth.signInWithEmailAndPassword(loginUser.username, loginUser.password).addOnSuccessListener {
                        val intent = Intent(this, WelcomeUserActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener {
                        username.error = it.message
                    }
                }

                LoginUser.LoginValidation.USERNAMEERROR ->  username.error = "Invalid Username."
                LoginUser.LoginValidation.PASSWORDERROR -> password.error = "Invalid Password."
            }

        }

    }
}