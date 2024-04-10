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
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ActivityLoginBinding

import com.example.myapplication.R
import kotlin.math.log

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

        username.visibility = View.GONE;
        password.visibility = View.GONE;
        login.visibility = View.GONE;
        phone.visibility = View.GONE;
        name.visibility = View.GONE;
        surname.visibility = View.GONE;
        text.visibility = View.GONE;
        doctorCheckBox.visibility = View.GONE;
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

            loginVk.visibility = View.VISIBLE
            loginGoogle.visibility = View.VISIBLE
            loginYandex.visibility = View.VISIBLE


            // Получаем расположение по высоте для username и password
            val usernameY = binding.username.y
            val passwordY = binding.password.y

            // Отображаем значения в консоли
            println("Username Y position: $usernameY")
            println("Password Y position: $passwordY")

            // Добавьте другие действия, которые должны произойти после нажатия на кнопку buttonLogin
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
            buttonLogin.visibility = View.VISIBLE
            buttonRegister.visibility = View.VISIBLE

            val halfScreenHeight = container.height / 2

            val slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            val slideInLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            val slideInRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)


            username.translationY = 0f
            password.translationY = 0f
            login2.translationY = -log2height
            phone.translationY = -phoneheight
            doctorCheckBox.translationY = -dockcheckheight



            name.visibility = View.GONE
            surname.visibility = View.GONE
            username.visibility = View.GONE
            password.visibility = View.GONE
            phone.visibility = View.GONE
            login2.visibility = View.GONE
            doctorCheckBox.visibility = View.GONE
            login.visibility = View.GONE
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

    private fun saveInitialParams() {
        // Сохраняем исходные параметры каждого элемента по их идентификаторам
        initialParamsSparseArray.put(R.id.username, binding.username.layoutParams)
        println("Saved initial params for username: ${binding.username.layoutParams}")

        initialParamsSparseArray.put(R.id.password, binding.password.layoutParams)
        println("Saved initial params for password: ${binding.password.layoutParams}")

        initialParamsSparseArray.put(R.id.name, name.layoutParams)
        println("Saved initial params for name: ${name.layoutParams}")

        initialParamsSparseArray.put(R.id.surname, surname.layoutParams)
        println("Saved initial params for surname: ${surname.layoutParams}")

        initialParamsSparseArray.put(R.id.login, binding.login.layoutParams)
        println("Saved initial params for login: ${binding.login.layoutParams}")
    }

    private fun restoreInitialParams() {
        // Восстанавливаем исходные параметры каждого элемента по их идентификаторам
        for (i in 0 until initialParamsSparseArray.size()) {
            val id = initialParamsSparseArray.keyAt(i)
            val view = findViewById<View>(id)
            view.layoutParams = initialParamsSparseArray.valueAt(i)
            println("Restored initial params for view with ID $id: ${initialParamsSparseArray.valueAt(i)}")
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
