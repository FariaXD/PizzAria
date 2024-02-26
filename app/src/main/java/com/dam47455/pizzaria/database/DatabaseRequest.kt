package com.dam47455.pizzaria.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseRequest {
    companion object{
        const val databaseURL = "https://pizzaria-dam-default-rtdb.europe-west1.firebasedatabase.app/"

        fun RequestNode(nodeName: String): DatabaseReference{
            return FirebaseDatabase.getInstance(databaseURL).getReference(nodeName)
        }

        fun GetValueFromNodeChild(databaseReference: DatabaseReference, child: String): String{
            var value = ""
            databaseReference.child(child).get().addOnSuccessListener {
                value = it.value.toString()
            }.addOnFailureListener {
                value = "ERROR: Couldn't find database."
            }
            return value
        }
    }
}