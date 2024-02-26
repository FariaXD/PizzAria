package com.dam47455.pizzaria.data

import java.io.Serializable
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

class FoodCart(var food: Food ?= null, var quantity: Int ?= null):Serializable {

    fun calculatePrice(): Float{
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP
        val priceCalculated: Float = quantity!! * (food!!.price!! - (food!!.price!! * (food!!.discount!!/100f)))
        return df.format(priceCalculated).toFloat()
    }
}