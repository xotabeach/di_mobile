package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contextualMenu: LinearLayout
    private lateinit var menuIcon: ImageView
    private var username = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        username = intent.getStringExtra("USERNAME").toString()
        password = intent.getStringExtra("PASSWORD").toString()

        contextualMenu = findViewById(R.id.contextualMenu)
        menuIcon = findViewById(R.id.menuIcon)


        contextualMenu.visibility = View.GONE
        val animate = TranslateAnimation(0f, -contextualMenu.width.toFloat()*2, 0f, 0f)
        animate.duration = 300
        animate.fillAfter = true
        contextualMenu.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                contextualMenu.visibility = View.GONE
            }
        })

        Log.d("MainActivity", "Username: $username, Password: $password")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))

        setupActionBarWithNavController(navController, appBarConfiguration)

        if (savedInstanceState == null) {
            navController.navigate(R.id.homeFragment)
        }
        binding.textApp.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }



        menuIcon.setOnClickListener {
            if (contextualMenu.visibility == View.GONE) {
                showMenu()
            } else {
                hideMenu()
            }
        }

        findViewById<TextView>(R.id.nav_profile).setOnClickListener {
            val bundle = Bundle().apply {
                putString("USERNAME", username)
                putString("PASSWORD", password)
            }
            navController.navigate(R.id.profileFragment, bundle)
            hideMenu()
        }
        findViewById<TextView>(R.id.nav_product_composition).setOnClickListener {
            hideMenu()
        }
        findViewById<TextView>(R.id.nav_menu).setOnClickListener {
            hideMenu()
    }
        findViewById<TextView>(R.id.nav_consultations).setOnClickListener {
            hideMenu()
        }
    }

    private fun showMenu() {
        contextualMenu.visibility = View.VISIBLE
        val animate = TranslateAnimation(-contextualMenu.width.toFloat()*2, 0f, 0f, 0f)
        animate.duration = 300
        animate.fillAfter = true
        contextualMenu.startAnimation(animate)
    }

    private fun hideMenu() {
        val animate = TranslateAnimation(0f, -contextualMenu.width.toFloat()*2, 0f, 0f)
        animate.duration = 300
        animate.fillAfter = true
        contextualMenu.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                contextualMenu.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        if (contextualMenu.visibility == View.VISIBLE) {
            hideMenu()
        } else {
            super.onBackPressed()
        }
    }
}
