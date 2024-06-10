package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.UserRepository
import com.example.myapplication.data.model.User
import com.example.myapplication.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val initialParamsSparseArray = SparseArray<ViewGroup.LayoutParams>()

    private lateinit var loginGoogle: LinearLayout
    private lateinit var loginYandex: LinearLayout
    private lateinit var loginVk: LinearLayout
    private lateinit var buttonLogin: Button
    private lateinit var name: TextView
    private lateinit var surname: TextView
    private lateinit var buttonRegister: Button
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var userheight: Float = 0.0f
    private var passheight: Float = 0.0f
    private var log2height: Float = 0.0f
    private var phoneheight: Float = 0.0f
    private var dockcheckheight: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginVk = binding.loginVk!!
        loginGoogle = binding.loginGoogle!!
        loginYandex = binding.loginYandex!!
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val login2 = binding.login2!!
        val loading = binding.loading
        val phone = binding.phone!!
        name = binding.name!!
        surname = binding.surname!!
        val text = binding.underlogotext!!
        val doctorCheckBox = binding.doctorCheckBox!!
        buttonLogin = binding.pushLogin!!
        buttonRegister = binding.pushRegister!!

        username.visibility = View.GONE
        password.visibility = View.GONE
        login.visibility = View.GONE
        phone.visibility = View.GONE
        name.visibility = View.GONE
        surname.visibility = View.GONE
        text.visibility = View.GONE
        doctorCheckBox.visibility = View.GONE
        login2.visibility = View.GONE
        loginVk.visibility = View.GONE
        loginYandex.visibility = View.GONE
        loginGoogle.visibility = View.GONE

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val container = binding.container

        saveInitialParams()

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val userRepository = UserRepository(this)

        buttonLogin.setOnClickListener {
            buttonLogin.visibility = View.GONE
            buttonRegister.visibility = View.GONE

            val slideInLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            val slideInRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)

            binding.username.visibility = View.VISIBLE
            binding.username.startAnimation(slideInLeftAnimation)

            binding.password.visibility = View.VISIBLE
            binding.password.startAnimation(slideInRightAnimation)

            binding.login.visibility = View.VISIBLE
            binding.login.startAnimation(slideInLeftAnimation)

            loginVk.visibility = View.VISIBLE
            loginGoogle.visibility = View.VISIBLE
            loginYandex.visibility = View.VISIBLE
        }

        buttonRegister.setOnClickListener {
            buttonLogin.visibility = View.GONE
            buttonRegister.visibility = View.GONE
            login.visibility = View.GONE
            val halfScreenHeight = container.height / 2

            val slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
            val slideInLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            val slideInRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)

            username.startAnimation(slideDownAnimation)
            password.startAnimation(slideDownAnimation)
            login2.startAnimation(slideDownAnimation)
            phone.startAnimation(slideDownAnimation)
            doctorCheckBox.startAnimation(slideDownAnimation)

            userheight = (halfScreenHeight.toFloat() - binding.username.height.toFloat()) / 2
            passheight = (halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 2
            log2height = ((halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 1.3).toFloat()
            phoneheight = (halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 2
            dockcheckheight = (halfScreenHeight.toFloat() - binding.password.height.toFloat()) / 2

            username.translationY = userheight
            password.translationY = passheight
            login2.translationY = log2height
            phone.translationY = phoneheight
            doctorCheckBox.translationY = dockcheckheight

            name.visibility = View.VISIBLE
            surname.visibility = View.VISIBLE
            username.visibility = View.VISIBLE
            password.visibility = View.VISIBLE
            phone.visibility = View.VISIBLE
            login2.visibility = View.VISIBLE
            doctorCheckBox.visibility = View.VISIBLE

            name.startAnimation(slideInLeftAnimation)
            surname.startAnimation(slideInRightAnimation)
        }

        login2.setOnClickListener {
            val usernameText = username.text.toString().trimStart()
            val passwordText = password.text.toString().trimStart()
            val nameText = name.text.toString().trimStart()
            val surnameText = surname.text.toString().trimStart()
            val phoneText = phone.text.toString().trimStart()
            val isDoctor = doctorCheckBox.isChecked

            if (usernameText.isNotEmpty() && passwordText.isNotEmpty() && nameText.isNotEmpty() && surnameText.isNotEmpty() && phoneText.isNotEmpty()) {
                lifecycleScope.launch {
                    val existingUser = userRepository.getUserByUsername(usernameText)
                    if (existingUser == null) {
                        val user = User(usernameText, passwordText, nameText, surnameText, phoneText, isDoctor)
                        userRepository.insertUser(user)
                        Toast.makeText(this@LoginActivity, "Registration successful", Toast.LENGTH_SHORT).show()


                    } else {
                        Toast.makeText(this@LoginActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        login.setOnClickListener {
            val usernameText = username.text.toString().trimStart()
            val passwordText = password.text.toString().trimStart()

            if (usernameText.isNotEmpty() && passwordText.isNotEmpty()) {
                lifecycleScope.launch {
                    val user = userRepository.getUserByUsername(usernameText)
                    if (user != null && user.password == passwordText) {
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            }
        }

        username.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString().trimStart(),
                    password.text.toString().trimStart()
                )
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    text.setText("")
                }
            }
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString().trimStart(),
                    password.text.toString().trimStart()
                )
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    text.setText("")
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString().trimStart(),
                            password.text.toString().trimStart()
                        )
                }
                false
            }
        }

        phone.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    text.setText("")
                }
            }
        }

        name.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    text.setText("")
                }
            }
        }

        surname.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    text.setText("")
                }
            }
        }
    }

    private fun saveInitialParams() {
        initialParamsSparseArray.put(R.id.username, binding.username.layoutParams)
        initialParamsSparseArray.put(R.id.password, binding.password.layoutParams)
        initialParamsSparseArray.put(R.id.name, name.layoutParams)
        initialParamsSparseArray.put(R.id.surname, surname.layoutParams)
        initialParamsSparseArray.put(R.id.login, binding.login.layoutParams)
    }

    private fun restoreInitialParams() {
        for (i in 0 until initialParamsSparseArray.size()) {
            val id = initialParamsSparseArray.keyAt(i)
            val view = findViewById<View>(id)
            view.layoutParams = initialParamsSparseArray.valueAt(i)
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
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

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
