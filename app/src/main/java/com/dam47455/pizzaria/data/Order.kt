package com.dam47455.pizzaria.data

import java.io.Serializable

class Order(var foodOrdered: ArrayList<FoodCart> ?= ArrayList(), var date: String ?= null, var delivery: Boolean ?= null) : Serializable
