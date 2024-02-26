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
import com.dam47455.pizzaria.adapters.CartPriceAdapter
import com.dam47455.pizzaria.data.FoodCart
import com.dam47455.pizzaria.database.DatabaseRequest
import com.dam47455.pizzaria.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CartFragment(var main: MainActivity) : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var fAuth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        fAuth = FirebaseAuth.getInstance()
        setDisplayedFood()
        setTabFood()
        updateCartIfEmpty()
        binding.proceedToCheckout.setOnClickListener{
            if(main.userInfo.shoppingCart!!.size > 0)
                main.changeFragment(CheckoutFragment(main))
            else
                Toast.makeText(binding.root.context, "No items in cart.", Toast.LENGTH_SHORT).show()
        }
        binding.ordersPage.setOnClickListener{
            main.changeFragment(OrdersFragment(main))
        }
        binding.clearShopCart.setOnClickListener{
            main.userInfo.shoppingCart!!.clear()
            setDisplayedFood()
            setTabFood()
            updateCartIfEmpty()
            main.updateBadge()
        }
        return binding.root
    }

    private fun setDisplayedFood(){
        val foodGrid = binding.cartFoodView
        foodGrid.adapter = CartItemAdapter(binding.root.context, main.userInfo.shoppingCart!!, this)
    }

    companion object{
        fun generateSimplifiedFoodItems(main: MainActivity) : ArrayList<FoodCart>{
            val tabItems = ArrayList<FoodCart>()
            main.userInfo.shoppingCart!!.forEach{ food ->
                var found = false
                if(tabItems.size > 0){
                    tabItems.forEach{ foodCart ->
                        if(food.name == foodCart.food!!.name){
                            foodCart.quantity = foodCart.quantity!! + 1
                            found = true
                        }
                    }
                }
                if(!found)
                    tabItems.add(FoodCart(food, 1))
            }
            return tabItems
        }
    }
    private fun setTabFood(){
        val tabGrid = binding.totalItems
        val simplifiedCart = generateSimplifiedFoodItems(main)
        tabGrid.adapter = CartPriceAdapter(binding.root.context,simplifiedCart)
        var totalQuant = 0
        var totalPrice = 0f
        simplifiedCart.forEach {
            totalQuant += it.quantity!!
            totalPrice += it.calculatePrice()
        }
        binding.numberOfItems.text = getString(R.string.totalItems, totalQuant)
        binding.totalPrice.text = getString(R.string.totalPrice, totalPrice)
    }

    fun removeItemFromCart(position: Int){
        main.userInfo.shoppingCart!!.removeAt(position)
        setDisplayedFood()
        setTabFood()
        updateCartIfEmpty()
        FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Users").child(fAuth.currentUser!!.uid).setValue(main.userInfo).addOnCompleteListener{
            if(it.isSuccessful)
                Toast.makeText(binding.root.context, "Removed Item", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(binding.root.context, "FAILED", Toast.LENGTH_SHORT).show()
        }
        main.updateBadge()
    }

    private fun updateCartIfEmpty(){
        if(main.userInfo.shoppingCart!!.size == 0){
            binding.proceedToCheckout.isEnabled = false
            binding.proceedToCheckout.text = getString(R.string.empty_cart)
        }
    }
}