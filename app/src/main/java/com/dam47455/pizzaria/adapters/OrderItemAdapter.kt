package com.dam47455.pizzaria.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.data.Order

class OrderItemAdapter(context: Context, var data: ArrayList<Order>) : ArrayAdapter<Order>(context, 0, data) {
    @SuppressLint("InflateParams")
    override fun getView(position: Int, itemView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.order_item, null, false)
        view.findViewById<TextView>(R.id.orderDate).text = data[position].date
        view.findViewById<TextView>(R.id.orderOption).text = if(data[position].delivery == true) context.getString(R.string.delivery) else context.getString(R.string.takeaway)
        view.findViewById<GridView>(R.id.orderFoodGrid).adapter = CartPriceAdapter(context, data[position].foodOrdered!!)
        return view
    }
}