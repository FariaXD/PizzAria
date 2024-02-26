package com.dam47455.pizzaria.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import java.io.Serializable

data class User(var firstName: String? = null, var lastName: String? = null,
                var email: String ?= null, var phone: String ?= null,
                var allergies: String ?= null, var address: String ?= null,
                var shoppingCart: ArrayList<Food> ?= ArrayList(),
                var orderList: ArrayList<Order> ?= ArrayList()
) : Serializable{

    fun processDataSnapshot(it: DataSnapshot){
        firstName = it.child(UserInfo.FIRSTNAME.databaseRef).getValue<String>()
        lastName = it.child(UserInfo.LASTNAME.databaseRef).getValue<String>()
        email = it.child(UserInfo.EMAIL.databaseRef).getValue<String>()
        phone = it.child(UserInfo.PHONE.databaseRef).getValue<String>()
        allergies = it.child(UserInfo.ALLERGIES.databaseRef).getValue<String>()
        address = it.child(UserInfo.ADDRESS.databaseRef).getValue<String>()
        shoppingCart = it.child(UserInfo.SHOP.databaseRef).getValue<ArrayList<Food>>()
        orderList = it.child(UserInfo.ORDER.databaseRef).getValue<ArrayList<Order>>()
    }

    fun countDeliveries(): String {
        var numb = 0
        orderList!!.forEach{
            if(it.delivery!!)
                numb++
        }
        return numb.toString()
    }

    companion object{
        enum class UserInfo(var databaseRef: String){
            FIRSTNAME("firstName"), LASTNAME("lastName"),
            EMAIL("email"), PHONE("phone"),
            ALLERGIES("allergies"), ADDRESS("address"),
            SHOP("shoppingCart"), ORDER("orderList")
        }
    }
}
