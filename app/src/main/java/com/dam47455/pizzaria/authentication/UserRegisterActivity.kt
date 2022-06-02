package com.dam47455.pizzaria.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.dam47455.pizzaria.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class UserRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)
        MainActivity.makeFullScreen(window)
        val fAuth = FirebaseAuth.getInstance()
        fAuth.signOut()

        findViewById<Button>(R.id.registerLoginBtn).setOnClickListener{
            var intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.registerSubmitBtn).setOnClickListener{
            val username = findViewById<EditText>(R.id.registerUsername)
            val email = findViewById<EditText>(R.id.registerEmail)
            val password = findViewById<EditText>(R.id.registerPassword)
            val cpassword = findViewById<EditText>(R.id.registerCPassword)
            val newUser = RegisterUser(username.text.toString(), email.text.toString(), password.text.toString(), cpassword.text.toString())
            when(newUser.validateInformation()){
                RegisterUser.RegisterValidation.NOERRORS -> {
                    fAuth.createUserWithEmailAndPassword(newUser.email, newUser.password).addOnSuccessListener {
                        val profile = UserProfileChangeRequest.Builder()
                            .setDisplayName(newUser.username)
                        val intent = Intent(this, WelcomeUserActivity::class.java)
                        intent.putExtra("username", newUser.username)
                        startActivity(intent)
                    }.addOnFailureListener {
                        findViewById<Button>(R.id.registerSubmitBtn).error = it.message
                    }

                }
                RegisterUser.RegisterValidation.USERNAMEERROR -> username.error = getString(R.string.incorrect_username)
                RegisterUser.RegisterValidation.EMAILERROR ->email.error = getString(R.string.incorrect_email)
                RegisterUser.RegisterValidation.PASSWORDERROR -> password.error = getString(R.string.incorrect_password)
                RegisterUser.RegisterValidation.CONFIRMPASSWORDERROR -> cpassword.error = getString(
                    R.string.incorrect_confirm_password
                )
            }

        }
    }
}