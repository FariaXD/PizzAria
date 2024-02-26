package com.dam47455.pizzaria.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.database.DatabaseRequest
import com.dam47455.pizzaria.databinding.FragmentCartBinding
import com.dam47455.pizzaria.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment(var main: MainActivity) : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var fAuth : FirebaseAuth
    private var editing = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setValues()
        fAuth = FirebaseAuth.getInstance()
        binding.editSaveProfile.setOnClickListener{
            setEditableOrSave()
        }
        return binding.root
    }

    private fun setValues(){
        binding.profileFirstLast.text = getString(R.string.firstLast, main.userInfo.firstName, main.userInfo.lastName)
        binding.profileOrderCount.text = main.userInfo.orderList!!.size.toString()
        binding.profileDeliveryCount.text = main.userInfo.countDeliveries()
        binding.profilePhoneNumber.setText(if(main.userInfo.phone == null) "" else main.userInfo.phone)
        binding.profileAddress.setText(if(main.userInfo.address == null) "" else main.userInfo.address)
        binding.profileAllergies.setText(if(main.userInfo.allergies == null) "" else main.userInfo.allergies)
    }


    private fun setEditableOrSave(){
        if(editing){
            main.userInfo.phone = binding.profilePhoneNumber.text.toString()
            main.userInfo.address = binding.profileAddress.text.toString()
            main.userInfo.allergies = binding.profileAllergies.text.toString()
            FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Users").child(fAuth.currentUser!!.uid).setValue(main.userInfo).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(binding.root.context, "Updated Profile", Toast.LENGTH_SHORT).show()
                    editing = false
                    updateButtonAndFields()
                }
                else
                    Toast.makeText(binding.root.context, "FAILED", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            editing = true
            updateButtonAndFields()
        }
    }

    private fun updateButtonAndFields() {
        if(editing)
            binding.editSaveProfile.text = getString(R.string.save)
        else
            binding.editSaveProfile.text = getString(R.string.edit)
        binding.profilePhoneNumber.isEnabled = editing
        binding.profileAddress.isEnabled = editing
        binding.profileAllergies.isEnabled = editing
    }
}