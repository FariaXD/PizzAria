package com.dam47455.pizzaria.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.authentication.FirstActivity
import com.dam47455.pizzaria.authentication.RegisterUser
import com.dam47455.pizzaria.authentication.WelcomeUserActivity
import com.dam47455.pizzaria.databinding.FragmentCheckoutBinding
import com.dam47455.pizzaria.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment(var main: MainActivity) : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var fAuth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        fAuth = FirebaseAuth.getInstance()
        binding.settingsLogout.setOnClickListener{
            settingLogout()
        }

        binding.changePasswordButton.setOnClickListener{
            val layoutCP = LayoutInflater.from(context).inflate(R.layout.change_password, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(layoutCP)
            val alert = builder.create()
            alert.show()
            layoutCP.findViewById<Button>(R.id.newPassRequest).setOnClickListener{
                val emailField = layoutCP.findViewById<EditText>(R.id.newPEmail)
                if(validateEmail(emailField.text.toString())){
                    fAuth.sendPasswordResetEmail(emailField.text.toString())
                    alert.cancel()
                }
                else{
                    emailField.error = getString(R.string.incorrect_email)
                }
            }

        }
        return binding.root
    }

    private fun settingLogout() {
        fAuth.signOut()
        startActivity(Intent(context, FirstActivity::class.java))
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}