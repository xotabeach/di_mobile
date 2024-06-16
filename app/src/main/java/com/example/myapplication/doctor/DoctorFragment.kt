package com.example.myapplication.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class DoctorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val faqList = listOf(
            DoctorItem(
                "Ахметзянова Гульнара Равилевна",
                "\n" +
                "1.Образование\n" +
                        "   2019 Г. - Казанский федеральный университет, \n" +
                        "лечебное дело\n" +
                        "2021 г. - Казанский федеральный университет,\n" +
                        "гастроэнтерология\n" +
                        "2.Повышение квалификации\n" +
                        "   2021 г . - “Гастроэнтерология сегодня”, Джи \n" +
                        "Академия\n" +
                        "2022 г. - “Бабочки в животе” курс Алексея Головенко,\n" +
                        "посвященный СРК\n" +
                        "2022 г. - “Актуальные вопросы диагностики и \n" +
                        "лечения заболеваний пищеварительной системы”\n" +
                        "3.Статьи в журнале\n" +
                        "   2020 г. - Европейский регистр ведения инфекций \n" +
                        "helicobacter pyloti: особенности диагностики и \n" +
                        "лечения в Казани."
            )
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = DoctorAdapter(faqList)
    }
}