package com.dam47455.pizzaria.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.data.Food
import com.dam47455.pizzaria.data.FoodCart
import com.dam47455.pizzaria.fragments.CartFragment
import com.squareup.picasso.Picasso

class CartItemAdapter(context: Context, var data: ArrayList<Food>, var main : CartFragment) : ArrayAdapter<Food>(context, 0, data) {
    @SuppressLint("InflateParams")
    override fun getView(position: Int, itemView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, null, false)
        view!!.findViewById<TextView>(R.id.nameFood).text = data[position].name
        view.findViewById<TextView>(R.id.priceFood).text = context.getString(R.string.price_food_item,data[position].calculatePrice().toString())
        view.findViewById<TextView>(R.id.descFood).text = data[position].desc
        view.findViewById<TextView>(R.id.nameFood).text = data[position].name
        val image = view.findViewById<ImageView>(R.id.imageFood)
        Picasso.get().load(data[position].image).into(image)
        view.findViewById<Button>(R.id.removeButton).setOnClickListener{
            main.removeItemFromCart(position)
        }
        if(data[position].discount!! > 0){
            view.findViewById<TextView>(R.id.priceFood).setTextColor(
                ContextCompat.getColor(context, R.color.discount)
            )
        }
        return view
    }
}