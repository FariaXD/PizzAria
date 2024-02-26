package com.dam47455.pizzaria

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.dam47455.pizzaria.data.User
import com.dam47455.pizzaria.fragments.*
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var selectedCategory: Category = Category.MEAT
    private lateinit var currentFragment: Fragment
    lateinit var bNav: BottomNavigationView
    lateinit var badge: BadgeDrawable
    lateinit var userInfo: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentFragment = HomeFragment(this)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, currentFragment).commit()
        makeFullScreen(window)
        prepareBottomNavBar()
        userInfo = intent.getSerializableExtra("User") as User
        if(userInfo.shoppingCart  == null){
            userInfo.shoppingCart = ArrayList()
        }
        if(userInfo.orderList == null){
            userInfo.orderList = ArrayList()
        }
        badge = bNav.getOrCreateBadge(R.id.cart)
        badge.isVisible = true
        badge.number = userInfo.shoppingCart!!.size
    }

    fun selectCategory(cat: String){
        when(cat){
            "Meat" -> selectedCategory = Category.MEAT
            "SeaFood" -> selectedCategory = Category.SEAFOOD
            "Vegan" -> selectedCategory = Category.VEGAN
            "Drinks" -> selectedCategory = Category.DRINKS
            "Extras" -> selectedCategory = Category.EXTRAS
        }
    }

    fun changeFragment(fragment: Fragment){
        currentFragment = fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, currentFragment).commit()
    }

    private fun prepareBottomNavBar(){
        bNav = findViewById(R.id.bottom_navigation)
        bNav.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.home -> currentFragment = HomeFragment(this)
                R.id.profile -> currentFragment = ProfileFragment(this)
                R.id.search -> currentFragment = SearchFragment(this)
                R.id.cart -> currentFragment = CartFragment(this)
                R.id.settings -> currentFragment = SettingsFragment(this)
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, currentFragment).commit()
            true
        }
    }

    fun updateBadge() {
        badge.number = userInfo.shoppingCart!!.size
    }

    companion object{
        enum class Category(var cat: String) {
            MEAT("Meat"), SEAFOOD("SeaFood"), VEGAN("Vegan"), DRINKS("Drinks"), EXTRAS("Extras")
        }
        enum class FoodElements(var el: String){
            DESC("Desc"), PRICE("Price"), DISCOUNT("Discount"), SOLD("Sold"), Image("Image")
        }
        fun makeFullScreen(window: Window) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }

            val windowInsetsController =
                ViewCompat.getWindowInsetsController(window.decorView) ?: return
            // Configure the behavior of the hidden system bars
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // Hide both the status bar and the navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}


