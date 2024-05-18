package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentMoreinfoBinding

class MoreInfoFragment : Fragment() {

    private var _binding: FragmentMoreinfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoreinfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение аргументов
        val title = arguments?.getString("title") ?: "No Title"

        // Установка текста в виджеты
        binding.textDescr.text = "Питание по разделам"
        binding.cardTitle1.text = title
        binding.cardText1.text = "Хирургическое вмешательство, подразумевающее тотальное (полное) удаление желудка с наложением пищеводно-кишечного соустья (анастомоза)."
        binding.cardTitle2.text = "Список продуктов"
        binding.cardTitle3.text = "Пищевые добавки"
        binding.cardTitle4.text = "Описание диеты"
        binding.cardTitle5.text = "Врач"

        // Обработчик нажатия на textApp
        binding.textApp.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
