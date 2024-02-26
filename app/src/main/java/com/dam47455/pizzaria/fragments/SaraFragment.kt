package com.dam47455.pizzaria.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.databinding.FragmentCartBinding
import com.dam47455.pizzaria.databinding.FragmentSaraBinding

class SaraFragment : Fragment() {
    private lateinit var binding: FragmentSaraBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaraBinding.inflate(layoutInflater)
        return binding.root
    }
}