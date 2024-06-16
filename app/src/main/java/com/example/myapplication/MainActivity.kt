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
    private lateinit var nav_profile:TextView
    private lateinit var nav_faq:TextView
    private lateinit var nav_help:TextView
    private var username = ""
    private var password = ""
    val bookmarks = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        username = intent.getStringExtra("USERNAME").toString()
        password = intent.getStringExtra("PASSWORD").toString()

        contextualMenu = findViewById(R.id.contextualMenu)
        menuIcon = findViewById(R.id.menuIcon)
        nav_profile = findViewById(R.id.nav_profile)
        nav_faq = findViewById(R.id.nav_faq)
        nav_help = findViewById(R.id.nav_help)


        nav_faq.isClickable = false
        nav_help.isClickable = false
        nav_profile.isClickable = false
        contextualMenu.visibility = View.GONE


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

        binding.innerCircle.setOnClickListener {
            navController.navigate(R.id.DietFragment)
        }

        binding.bookmarkIcon.setOnClickListener {
            navController.navigate(R.id.BookmarkFragment)
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
            hideMenu()
            navController.navigate(R.id.profileFragment, bundle)

        }
        findViewById<TextView>(R.id.nav_faq).setOnClickListener {

            navController.navigate(R.id.FAQFragment)
            hideMenu()
        }
        findViewById<TextView>(R.id.nav_help).setOnClickListener {
            navController.navigate(R.id.HelpFragment)
            hideMenu()
    }

    }

    private fun showMenu() {
        contextualMenu.visibility = View.VISIBLE
        nav_profile.isClickable = true
        nav_faq.isClickable = true
        nav_help.isClickable = true

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
                nav_profile.isClickable = false
                nav_faq.isClickable = false
                nav_help.isClickable = false
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
