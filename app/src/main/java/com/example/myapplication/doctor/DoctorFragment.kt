package com.example.myapplication.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

/**
 * A simple [Fragment] subclass.
 * Use the [DoctorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
                "Как сменить язык?",
                "Для смены языка перейдите в настройки и выберите нужный язык."
            ),
            DoctorItem(
                "Как создать маршрут?",
                "Для создания маршрута используйте вкладку 'Маршруты' и следуйте инструкциям."
            ),
            DoctorItem(
                "Я не могу создавать маршрут",
                "Пожалуйста, убедитесь, что у вас есть права на создание маршрута."
            ),
            DoctorItem(
                "Как сменить данные профиля?",
                "Для смены данных профиля перейдите в раздел 'Профиль' и выберите пункт 'Редактировать'."
            )
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = DoctorAdapter(faqList)
    }
}