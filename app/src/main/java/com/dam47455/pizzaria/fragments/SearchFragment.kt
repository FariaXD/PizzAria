package com.dam47455.pizzaria.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.database.DatabaseRequest
import com.dam47455.pizzaria.data.Food
import com.dam47455.pizzaria.databinding.FragmentSearchBinding
import com.dam47455.pizzaria.widgets.Item
import com.dam47455.pizzaria.adapters.ItemAdapter
import com.dam47455.pizzaria.widgets.SlowyLinearLayoutManager
import com.dam47455.pizzaria.adapters.SearchItemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso


class SearchFragment(private var main:MainActivity) : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: ItemAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fAuth : FirebaseAuth
    private var foodList = ArrayList<Food>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        setItemSliders()
        displayFoodOnView()
        fAuth = FirebaseAuth.getInstance()
        binding.categoryName.text = main.selectedCategory.name
        return binding.root
    }

    private fun setDisplayedFood(){
        val foodGrid = binding.foodDisplayer
        foodGrid.adapter = SearchItemAdapter(binding.root.context, foodList, this)
    }

    private fun displayFoodOnView() {
        databaseReference = FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Food").child(main.selectedCategory.cat)
            databaseReference.get().addOnSuccessListener { foodListDB ->
            foodListDB.children.forEach{
                val desc = it.child(MainActivity.Companion.FoodElements.DESC.el).getValue<String>()
                val price = it.child(MainActivity.Companion.FoodElements.PRICE.el).getValue<Float>()
                val disc = it.child(MainActivity.Companion.FoodElements.DISCOUNT.el).getValue<Int>()
                val sold = it.child(MainActivity.Companion.FoodElements.SOLD.el).getValue<Int>()
                val image = it.child(MainActivity.Companion.FoodElements.Image.el).getValue<String>()
                foodList.add(Food(it.key, desc, price, disc, sold, image))
            }

        }.addOnCompleteListener{
                setDisplayedFood()
        }
    }

    fun addItemToCart(food: Food) {
        main.userInfo.shoppingCart!!.add(food)
        FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Users").child(fAuth.currentUser!!.uid).setValue(main.userInfo).addOnCompleteListener{
            if(it.isSuccessful)
                Toast.makeText(binding.root.context, "Added to cart", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(binding.root.context, "FAILED", Toast.LENGTH_SHORT).show()
        }
        main.updateBadge()
    }

    private fun setItemSliders(){
        val list = listOf(
            Item(R.drawable.meat, "Meat", R.color.meatC),
            Item(R.drawable.seafood, "SeaFood", R.color.seaC),
            Item(R.drawable.vegan, "Vegan", R.color.veganC),
            Item(R.drawable.extras, "Extras", R.color.extrasC),
            Item(R.drawable.drinks, "Drinks", R.color.drinksC)
        )

        adapter = ItemAdapter(list, main)

        val timer = object : CountDownTimer(2000, 1000){
            override fun onTick(p0: Long) {
                binding.categoriesScrollSearch.post{
                    binding.categoriesScrollSearch.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onFinish() {
                binding.categoriesScrollSearch.post{
                    binding.categoriesScrollSearch.smoothScrollToPosition(0)
                }
            }
        }.start()

        object : CountDownTimer(1000, 1000){
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
                timer.start()
            }
        }.start()

        binding.categoriesScrollSearch.layoutManager = SlowyLinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesScrollSearch.adapter = adapter
    }
}