package com.dam47455.pizzaria.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.adapters.CartItemAdapter
import com.dam47455.pizzaria.adapters.OrderItemAdapter
import com.dam47455.pizzaria.databinding.FragmentCartBinding
import com.dam47455.pizzaria.databinding.FragmentOrdersBinding
import com.google.firebase.auth.FirebaseAuth


class OrdersFragment(var main: MainActivity) : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater)
        binding.cartPage.setOnClickListener{
            main.changeFragment(CartFragment(main))
        }
        setOrderList()
        return binding.root
    }

    private fun setOrderList() {
        val orderGrid = binding.orderGridView
        orderGrid.adapter = OrderItemAdapter(binding.root.context, main.userInfo.orderList!!)
    }
}