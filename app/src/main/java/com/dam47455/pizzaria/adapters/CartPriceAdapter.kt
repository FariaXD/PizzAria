package com.dam47455.pizzaria.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.data.Food
import com.dam47455.pizzaria.data.FoodCart
import com.dam47455.pizzaria.fragments.CartFragment

class CartPriceAdapter(context: Context, var data: ArrayList<FoodCart>) : ArrayAdapter<FoodCart>(context, 0, data) {
    @SuppressLint("InflateParams")
    override fun getView(position: Int, itemView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_total_item, null, false)
        view!!.findViewById<TextView>(R.id.totalItemName).text = data[position].food!!.name
        view.findViewById<TextView>(R.id.totalNumItem).text = data[position].quantity.toString()
        view.findViewById<TextView>(R.id.totalPriceItem).text = context.getString(R.string.price_food_item,data[position].calculatePrice().toString())
        return view
    }
}