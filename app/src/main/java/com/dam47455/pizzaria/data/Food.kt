package com.dam47455.pizzaria.data

import android.widget.ImageView
import java.io.Serializable
import java.math.RoundingMode
import java.text.DecimalFormat

class Food(var name: String ?= null, var desc: String ?= null, var price: Float ?= null, var discount: Int ?= null, var sold: Int ?= null, var image: String?= null) : Serializable{
    fun calculatePrice(): Float{
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP
        val priceCalculated = (price!! - (price!! * (discount!! / 100f)))
        return df.format(priceCalculated).toFloat()
    }
}