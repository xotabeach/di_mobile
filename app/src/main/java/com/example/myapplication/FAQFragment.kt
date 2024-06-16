package com.example.myapplication

import FAQAdapter
import FAQItem


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R

class FAQFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var faqAdapter: FAQAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_faq, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewfaq)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val faqList = listOf(
            FAQItem("Как создать диету?", "Здесь будет описание причин..."),
            FAQItem("Как редактировать профиль?", "Здесь будет описание лечения..."),
            FAQItem("Как посмотреть закладки?", "Здесь будут основные принципы лечения...")
        )

        faqAdapter = FAQAdapter(faqList)
        recyclerView.adapter = faqAdapter

        return view
    }
}
