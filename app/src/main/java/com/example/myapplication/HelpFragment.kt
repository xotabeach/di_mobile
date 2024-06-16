package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class HelpFragment : Fragment() {

    private lateinit var userText: EditText
    private lateinit var notNowTextView: TextView
    private lateinit var sendTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_help, container, false)

        userText = view.findViewById(R.id.userText)
        notNowTextView = view.findViewById(R.id.notNowTextView)
        sendTextView = view.findViewById(R.id.sendTextView)

        notNowTextView.setOnClickListener {
            userText.text.clear()
            Toast.makeText(requireContext(), "Текст очищен", Toast.LENGTH_SHORT).show()
        }

        sendTextView.setOnClickListener {
            val text = userText.text.toString().trim()
            if (text.isNotEmpty()) {
                sendEmail(text)
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, введите текст", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendEmail(text: String) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "plain/text"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("your_email@example.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Просьба клиента")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(emailIntent, "Отправить email..."))
    }
}
