package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.DatabaseHelper
import com.example.myapplication.databinding.FragmentDescriptionDietBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DescriptionDietFragment : Fragment() {

    private var _binding: FragmentDescriptionDietBinding? = null
    private val binding get() = _binding!!

    private lateinit var faqCardHolder: LinearLayout
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDescriptionDietBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        faqCardHolder = binding.faqCardHolder

        val title = arguments?.getString("title")
        val texts = arrayOf(
            "Причины",
            "Лечение",
            "Основные принципы лечения"
        )

        Log.d("title", "title is : $title")
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.moreInfoFragment, bundle)
        }




        // Здесь добавим запрос к базе данных для получения данных по title
        if (title != null) {
            getDiseaseData(title) { reasons, treatment, principles ->
                val data = arrayOf(reasons, treatment, principles)
                createCardView(texts, data)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createCardView(texts: Array<String>, data: Array<String>) {
        for (i in texts.indices) {
            val cardIndex = i // Идентификатор карточки

            // Создание макета для карточки и вложенного макета
            val container = LinearLayout(context)
            val containerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            containerParams.setMargins(22, -3, 22, 0)
            container.orientation = LinearLayout.VERTICAL
            container.layoutParams = containerParams

            val iconState = booleanArrayOf(true)

            // Создание карточки
            val cardView = CardView(requireContext())
            val cardParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardView.layoutParams = cardParams
            cardView.useCompatPadding = true
            cardView.setCardBackgroundColor(ResourcesCompat.getColor(resources, android.R.color.white, null))
            cardView.cardElevation = 10f
            cardView.radius = 32f
            cardView.z = 8f

            // Создание макета для элементов внутри карточки
            val innerLayout = LinearLayout(requireContext())
            innerLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            innerLayout.orientation = LinearLayout.HORIZONTAL
            innerLayout.gravity = Gravity.CENTER_VERTICAL or Gravity.END // Выравнивание по вертикали и прижатие к правому краю

            // Создание текстового представления
            val textView = TextView(requireContext())
            val textParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            textParams.setMargins(55, 65, 0, 65) // Отступы
            textView.layoutParams = textParams
            textView.textSize = 16f
            textView.text = texts[i] // Установка текста из массива

            // Создание иконки "+"
            val plusIcon = ImageView(requireContext())
            val iconParams = LinearLayout.LayoutParams(70, 70)
            iconParams.setMargins(0, 15, 55, 15)
            plusIcon.layoutParams = iconParams
            plusIcon.setImageResource(R.drawable.down_arrow)
            plusIcon.setPadding(1, 1, 1, 1)

            // Добавление элементов в макет
            innerLayout.addView(textView)
            innerLayout.addView(plusIcon)

            // Добавление макета в карточку
            cardView.addView(innerLayout)



            // Создание вложенного макета для текста
            val nestedFrameLayout = LinearLayout(requireContext())
            val nestedParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            nestedParams.setMargins(25, -85, 25, 15)

            nestedFrameLayout.layoutParams = nestedParams
            nestedFrameLayout.setBackgroundResource(R.drawable.one_more_corner_back)// Устанавливаем белый фоновый цвет
            nestedFrameLayout.visibility = View.GONE
            nestedFrameLayout.z = 4f
            val nestedTextView = TextView(requireContext())
            val nestedTextParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            nestedTextView.textSize = 15f
            nestedTextParams.setMargins(55, 75, 55, 45)
            nestedTextView.layoutParams = nestedTextParams
            nestedTextView.text = data[i] // Установка текста из данных

            // Установка параметра междустрочного интервала
            val lineSpacingExtra = 8f // Задайте нужное значение межстрочного интервала в пикселях
            nestedTextView.setLineSpacing(lineSpacingExtra, 1.0f)

            // Добавление элементов во вложенный макет
            nestedFrameLayout.addView(nestedTextView)

            // Добавление карточки и вложенного макета в контейнер
            container.addView(cardView)
            container.addView(nestedFrameLayout)

            // Добавление обработчика щелчка
            innerLayout.setOnClickListener {
                iconState[0] = !iconState[0]
                if (iconState[0]) {
                    plusIcon.setImageResource(R.drawable.down_arrow)
                    nestedFrameLayout.visibility = View.GONE
                } else {
                    plusIcon.setImageResource(R.drawable.upload)
                    nestedFrameLayout.visibility = View.VISIBLE
                }
            }

            // Добавление контейнера в faqCardHolder
            faqCardHolder.addView(container)
        }
    }

    private fun getDiseaseData(title: String, callback: (String, String, String) -> Unit) {
        val disease = dbHelper.getDiseaseDataByName(title)
        if (disease != null) {
            callback(disease.causes, disease.treatment, disease.diagnostics)
        } else {
            callback("", "", "")
        }
    }
}
