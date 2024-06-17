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
            FAQItem("Как создать диету?", "Для того, чтобы создать диету, в нижней навигационной панели нажмите на знак +, который перенесет вас в экран создания меню.\n" +
                    "Далее выбираете вашу болезнь, вписываете продукты, которые вам не нужны и жмёте кнопку создать диету"),
            FAQItem("Как редактировать профиль?", "В меню профиля, справа от вашей фотографии профиля, есть контекстные три точки, нажав на которые, вы попадёте в меню редактирования данных профиля"),
            FAQItem("Как посмотреть закладки?", "Для того, чтобы просмотреть закладки, в нижней навигационной панели нажмите на самый правый знак Закладка, после нажатия вы попадаете в меню закладок")
        )

        faqAdapter = FAQAdapter(faqList)
        recyclerView.adapter = faqAdapter

        return view
    }
}
