package com.dam47455.pizzaria.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.R
import com.dam47455.pizzaria.databinding.FragmentHomeBinding
import com.dam47455.pizzaria.widgets.Item
import com.dam47455.pizzaria.adapters.ItemAdapter
import com.dam47455.pizzaria.adapters.SearchItemAdapter
import com.dam47455.pizzaria.data.Food
import com.dam47455.pizzaria.database.DatabaseRequest
import com.dam47455.pizzaria.widgets.SlowyLinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso

class HomeFragment(private var main: MainActivity) : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ItemAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.userName.text = getString(R.string.home_user_name, main.userInfo.firstName)
        fAuth = FirebaseAuth.getInstance()
        setItemSliders()
        setPromotionalOffers()
        getMostPopularFood()
        //main.bNav.selectedItemId = R.id.home
        main.updateBadge()
        return binding.root
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
                binding.categoriesScroll.post{
                    binding.categoriesScroll.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onFinish() {
                binding.categoriesScroll.post{
                    binding.categoriesScroll.smoothScrollToPosition(0)
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

        binding.categoriesScroll.layoutManager = SlowyLinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesScroll.adapter = adapter
    }

    private fun setPromotionalOffers(){
        val promotionalOffersSlider = binding.promotionalImages
        val promotionalOfferList = ArrayList<SlideModel>()
        promotionalOfferList.add(SlideModel(R.drawable.promotional1))
        promotionalOfferList.add(SlideModel(R.drawable.promotional2))
        promotionalOffersSlider.setImageList(promotionalOfferList, ScaleTypes.FIT)
    }

    private fun getMostPopularFood(){
        var tmpFood = Food("tmp", "tmp", 0f, 0, 0)
        databaseReference = FirebaseDatabase.getInstance(DatabaseRequest.databaseURL).getReference("Food")
        databaseReference.get().addOnSuccessListener { foodListDB ->
            foodListDB.children.forEach{cat ->
                cat.children.forEach{ item ->
                    val sold = item.child(MainActivity.Companion.FoodElements.SOLD.el).getValue<Int>()
                    if(sold != null && sold > tmpFood.sold!!){
                        val desc = item.child(MainActivity.Companion.FoodElements.DESC.el).getValue<String>()
                        val price = item.child(MainActivity.Companion.FoodElements.PRICE.el).getValue<Float>()
                        val disc = item.child(MainActivity.Companion.FoodElements.DISCOUNT.el).getValue<Int>()
                        val image = item.child(MainActivity.Companion.FoodElements.Image.el).getValue<String>()
                        tmpFood = Food(item.key, desc, price, disc, sold, image)
                    }
                }
            }

        }.addOnCompleteListener{
            setDisplayedPopular(tmpFood)
        }
    }

    private fun setDisplayedPopular(food: Food){
        val tmp = ArrayList<Food>()
        tmp.add(food)
        val foodGrid = binding.foodHomeDisplayer
        foodGrid.adapter = SearchItemAdapter(binding.root.context, tmp, home = this)
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
}