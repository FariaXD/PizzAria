package com.dam47455.pizzaria.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.adapters.CartPriceAdapter
import com.dam47455.pizzaria.data.Order
import com.dam47455.pizzaria.database.DatabaseRequest
import com.dam47455.pizzaria.databinding.FragmentCheckoutBinding
import com.dam47455.pizzaria.fragments.CartFragment.Companion.generateSimplifiedFoodItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter


class CheckoutFragment(var main: MainActivity) : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var fAuth : FirebaseAuth
    private val CARDNUMBERS = 16
    private val CARDSECURITY = 3
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(layoutInflater)
        fAuth = FirebaseAuth.getInstance()
        setTabFood()
        binding.checkoutButton.setOnClickListener {
            prepareCheckout()
        }
        return binding.root
    }

    private fun prepareCheckout(){
        if((main.userInfo.address == null && !binding.takeawayCheckout.isChecked) || (main.userInfo.address!!.isEmpty() && !binding.takeawayCheckout.isChecked)){
            sendUserAddressRequest()
        }
        else{
            if(binding.cardNumber.text!!.toString().replace(" ", "").length == CARDNUMBERS && binding.securityCode.text.length == CARDSECURITY && validateExpireDate()){
                createOrder()
            }
            else{
                val layoutCP = LayoutInflater.from(context).inflate(R.layout.credit_card_incorrect, null)
                val builder = AlertDialog.Builder(context)
                builder.setView(layoutCP)
                val alert = builder.create()
                alert.show()
                layoutCP.findViewById<Button>(R.id.confirmCreditCardIncorrect).setOnClickListener{
                    alert.cancel()
                }
            }
        }
    }

    private fun sendUserAddressRequest(){
        val layoutCP = LayoutInflater.from(context).inflate(R.layout.user_address_request, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(layoutCP)
        val alert = builder.create()
        alert.show()
        layoutCP.findViewById<Button>(R.id.saveAddress).setOnClickListener{
            val addressChosen = layoutCP.findViewById<EditText>(R.id.newAddress).text.toString()
            if(addressChosen.isNotEmpty()){
                main.userInfo.address = addressChosen
                FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Users").child(fAuth.currentUser!!.uid).setValue(main.userInfo).addOnCompleteListener{
                    val messageLayout = LayoutInflater.from(context).inflate(R.layout.dialog_message, null)
                    val messageBuilder = AlertDialog.Builder(context)
                    messageBuilder.setView(messageLayout)
                    val alertMessage = messageBuilder.create()
                    messageLayout.findViewById<Button>(R.id.continueButton).setOnClickListener{
                        alertMessage.cancel()
                    }
                    if(it.isSuccessful){
                        alert.cancel()
                        alertMessage.show()
                    }
                    else{
                        messageLayout.findViewById<TextView>(R.id.mainMessage).text = getString(R.string.failed)
                    }
                }
            }
        }
        layoutCP.findViewById<Button>(R.id.cancelAddress).setOnClickListener{
            alert.cancel()
        }
    }

    private fun validateExpireDate(): Boolean {
        val date = binding.expirationDate.text!!.split("/")
        val cardMonth = date[0].toInt()
        val cardYear = date[1].toInt()
        val curMonth = LocalDateTime.now().month.ordinal + 1
        val curYear = LocalDateTime.now().year.toString().drop(2).toInt()
        return validateDate(cardMonth, cardYear) && ((cardYear > curYear) || (cardYear == curYear && cardMonth >= curMonth))
    }

    private fun validateDate(cardMonth : Int, cardYear: Int): Boolean{
        return cardMonth in 1..12 && cardYear in 0..50
    }

    private fun createOrder(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val date = current.format(formatter)
        main.userInfo.orderList!!.add(Order(generateSimplifiedFoodItems(main), date, !binding.takeawayCheckout.isChecked))
        main.userInfo.shoppingCart!!.clear()
        FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Users").child(fAuth.currentUser!!.uid).setValue(main.userInfo).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(binding.root.context, "Checkout complete!", Toast.LENGTH_SHORT).show()
                main.updateBadge()
                main.changeFragment(OrdersFragment(main))
            }
            else
                Toast.makeText(binding.root.context, "FAILED", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTabFood(){
        val tabGrid = binding.checkoutCartItems
        val simplifiedCart = generateSimplifiedFoodItems(main)
        tabGrid.adapter = CartPriceAdapter(binding.root.context,simplifiedCart)
        var totalQuant = 0
        var totalPrice = 0f
        simplifiedCart.forEach {
            totalQuant += it.quantity!!
            totalPrice += it.calculatePrice()
        }
        binding.checkoutNumberOfItems.text = getString(R.string.totalItems, totalQuant)
        binding.checkoutPriceTotal.text = getString(R.string.totalPrice, totalPrice)
    }
}