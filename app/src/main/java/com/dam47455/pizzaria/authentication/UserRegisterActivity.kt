package com.dam47455.pizzaria.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.authentication.MainActivity.Companion.databaseURL
import com.dam47455.pizzaria.authentication.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserRegisterActivity : AppCompatActivity() {
    private lateinit var fAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)
        MainActivity.makeFullScreen(window)
        fAuth = FirebaseAuth.getInstance()


        findViewById<Button>(R.id.registerLoginBtn).setOnClickListener{
            var intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.registerSubmitBtn).setOnClickListener{
            val firstName = findViewById<EditText>(R.id.registerFirstName)
            val lastName = findViewById<EditText>(R.id.registerLastName)
            val email = findViewById<EditText>(R.id.registerEmail)
            val password = findViewById<EditText>(R.id.registerPassword)
            val cpassword = findViewById<EditText>(R.id.registerCPassword)
            val newUser = RegisterUser(firstName.text.toString(), lastName.text.toString(), email.text.toString(), password.text.toString(), cpassword.text.toString())
            when(newUser.validateInformation()){
                RegisterUser.RegisterValidation.NOERRORS -> {
                   registerUser(newUser)
                }
                RegisterUser.RegisterValidation.FIRSTNAMEERROR -> firstName.error = getString(R.string.incorrect_username)
                RegisterUser.RegisterValidation.LASTNAMEERROR -> firstName.error = getString(R.string.incorrect_username)
                RegisterUser.RegisterValidation.EMAILERROR ->email.error = getString(R.string.incorrect_email)
                RegisterUser.RegisterValidation.PASSWORDERROR -> password.error = getString(R.string.incorrect_password)
                RegisterUser.RegisterValidation.CONFIRMPASSWORDERROR -> cpassword.error = getString(
                    R.string.incorrect_confirm_password
                )
            }

        }
    }

    private fun registerUser(newUser: RegisterUser){
        fAuth.createUserWithEmailAndPassword(newUser.email, newUser.password).addOnSuccessListener {
            databaseReference = FirebaseDatabase.getInstance(databaseURL).getReference("Users")
            val userInfo = User(newUser.firstName, newUser.lastName, newUser.email)
            Toast.makeText(this, fAuth.currentUser!!.uid, Toast.LENGTH_SHORT).show()
            databaseReference.child(fAuth.currentUser!!.uid).setValue(userInfo).addOnCompleteListener{
                if(it.isSuccessful)
                    Toast.makeText(this, "Created USERS", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, WelcomeUserActivity::class.java)
            intent.putExtra("firstName", newUser.firstName)
            startActivity(intent)
        }.addOnFailureListener {
            findViewById<Button>(R.id.registerSubmitBtn).error = it.message
        }
    }
}