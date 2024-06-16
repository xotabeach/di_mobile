package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.DatabaseHelper
import com.example.myapplication.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        val exitButton = view.findViewById<LinearLayout>(R.id.exitButton)
        exitButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val moreButton = view.findViewById<ImageView>(R.id.imageMore)
        moreButton.setOnClickListener {
            findNavController().navigate(R.id.ExpandedProfileFragment)
        }

        val supportText = view.findViewById<TextView>(R.id.text2)
        supportText.setOnClickListener {
            showBottomSheetDialog()
        }

        return view
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
        val bottomSheetView = layoutInflater.inflate(R.layout.popup_window, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetView.findViewById<TextView>(R.id.button_whatsapp).setOnClickListener {
            Toast.makeText(requireContext(), "WhatsApp clicked", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.button_telegram).setOnClickListener {
            Toast.makeText(requireContext(), "Telegram clicked", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.button_cancel).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}
