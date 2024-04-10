package com.example.myapplication.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ActivityLoginBinding

import com.example.myapplication.R
import kotlin.math.log

class LoginActivity : AppCompatActivity() {


    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val phone = binding.phone!!
        val name = binding.name!!
        val surname = binding.surname!!
        val text = binding.underlogotext!!
        val doctorCheckBox = binding.doctorCheckBox!!
        buttonLogin = binding.pushLogin!!
        buttonRegister = binding.pushRegister!!

        username.visibility = View.GONE;
        password.visibility = View.GONE;
        login.visibility = View.GONE;
        phone.visibility = View.GONE;
        name.visibility = View.GONE;
        surname.visibility = View.GONE;
        text.visibility = View.GONE;
        doctorCheckBox.visibility = View.GONE;

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val container = binding.container



        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer { loginResult ->
            loginResult ?: return@Observer

            binding.loading?.visibility = View.GONE

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            } else if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })


        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        buttonLogin.setOnClickListener {
            buttonLogin.visibility = View.GONE
            buttonRegister?.visibility = View.GONE

            val slideInLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            val slideInRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)

            binding.username.visibility = View.VISIBLE
            binding.username.startAnimation(slideInLeftAnimation)

            binding.password.visibility = View.VISIBLE
            binding.password.startAnimation(slideInRightAnimation)

            binding.login.visibility = View.VISIBLE
            binding.login.startAnimation(slideInLeftAnimation)

            // Добавьте другие действия, которые должны произойти после нажатия на кнопку buttonLogin
        }

        buttonRegister.setOnClickListener {
            buttonLogin.visibility = View.GONE
            buttonRegister.visibility = View.GONE

            val halfScreenHeight = container.height / 2

            val slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
            val slideInLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            val slideInRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)

            username.startAnimation(slideDownAnimation)
            password.startAnimation(slideDownAnimation)
            login.startAnimation(slideDownAnimation)
            phone.startAnimation(slideDownAnimation)
            doctorCheckBox.startAnimation(slideDownAnimation)

            username.translationY = (halfScreenHeight.toFloat() - binding.username.height.toFloat()) / 2
            password.translationY = (halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 2
            login.translationY = ((halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 1.3).toFloat()
            phone.translationY = (halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 2
            doctorCheckBox.translationY = (halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 2


            name.visibility = View.VISIBLE
            surname.visibility = View.VISIBLE
            username.visibility = View.VISIBLE
            password.visibility = View.VISIBLE
            phone.visibility = View.VISIBLE
            login.visibility = View.VISIBLE
            doctorCheckBox.visibility = View.VISIBLE

            name.startAnimation(slideInLeftAnimation)
            surname.startAnimation(slideInRightAnimation)

        }




        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                binding.loading?.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}