package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.data.model.DatabaseHelper

class ProfileFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        // Получаем данные из Bundle
        val username = arguments?.getString("USERNAME")
        val password = arguments?.getString("PASSWORD")

        if (username != null && password != null) {
            val user = databaseHelper.getUser(username, password)
            if (user != null) {
                view.findViewById<TextView>(R.id.name).text = user.name
                view.findViewById<TextView>(R.id.surname).text = user.surname

                // Добавьте отображение других полей, если необходимо
            }
        }

        return view
    }
}
