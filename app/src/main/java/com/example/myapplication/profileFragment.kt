package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.DatabaseHelper
import com.example.myapplication.data.model.User

class ProfileFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        val username = arguments?.getString("USERNAME")
        val password = arguments?.getString("PASSWORD")

        if (username != null && password != null) {
            val user = databaseHelper.getUser(username, password)
            if (user != null) {
                userEmail = user.mail
                val nameTextView = view.findViewById<TextView>(R.id.name)
                val surnameTextView = view.findViewById<TextView>(R.id.surname)
                val doctorStatus = if (user.isDoctor) "Врач" else "Пациент"

                nameTextView.text = "${user.name} ${user.surname}"
                surnameTextView.text = doctorStatus
            }
        }

        val moreButton = view.findViewById<ImageView>(R.id.imageMore)
        moreButton.setOnClickListener {

            findNavController().navigate(R.id.ExpandedProfileFragment)
        }

        return view
    }
}
