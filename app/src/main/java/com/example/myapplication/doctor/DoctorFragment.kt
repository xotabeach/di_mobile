package com.example.myapplication.doctor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class DoctorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_doctor, container, false)
        val title = arguments?.getString("title")
        Log.d("title", "title is : $title")


        val backButton = view?.findViewById<ImageView>(R.id.backButton)
        backButton?.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.moreInfoFragment, bundle)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val faqList = listOf(
            DoctorItem(
                "Ахметзянова Гульнара Равилевна",
                "\n" +
                "1.Образование\n" +
                        "   \n2019 Г. - Казанский федеральный университет, \n" +
                        "лечебное дело\n" +
                        "2021 г. - Казанский федеральный университет,\n" +
                        "гастроэнтерология\n" +
                        "\n2.Повышение квалификации\n" +
                        "   \n2021 г . - “Гастроэнтерология сегодня”, Джи \n" +
                        "Академия\n" +
                        "2022 г. - “Бабочки в животе” курс Алексея Головенко,\n" +
                        "посвященный СРК\n" +
                        "2022 г. - “Актуальные вопросы диагностики и \n" +
                        "лечения заболеваний пищеварительной системы”\n" +
                        "\n3.Статьи в журнале\n" +
                        "    \n2020 г. - Европейский регистр ведения инфекций \n" +
                        "helicobacter pyloti: особенности диагностики и \n" +
                        "лечения в Казани."
            )
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = DoctorAdapter(faqList)
    }
}