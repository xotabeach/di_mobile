package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.Disease.diseases
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

        val disease = diseases.find { it.name == title }
        // Установка текста в виджеты
        disease?.let {
            binding.textDescr.text = "Питание по разделам"
            binding.cardTitle1.text = title
            binding.cardText1.text = it.description
            binding.cardTitle2.text = "Список продуктов"
            binding.cardTitle3.text = "Пищевые добавки"
            binding.cardTitle4.text = "Описание диеты"
            binding.cardTitle5.text = "Врач"
        }

        // Обработчики нажатий на cardView с передачей аргумента
        binding.cardView3.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.componentsFragment, bundle)
        }

        binding.cardView2.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.productsFragment, bundle)
        }

        binding.cardView4.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.DescriptionDietFragment, bundle)
        }

        binding.cardView5.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.DoctorFragment, bundle)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
