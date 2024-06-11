package com.example.myapplication

import android.os.Bundle
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
import com.example.myapplication.databinding.FragmentDescriptionDietBinding

class DescriptionDietFragment : Fragment() {

    private var _binding: FragmentDescriptionDietBinding? = null
    private val binding get() = _binding!!

    private lateinit var faqCardHolder: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDescriptionDietBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")

        faqCardHolder = binding.faqCardHolder

        val texts = arrayOf(
            "Причины",
            "Лечение",
            "Основные принципы лечения"
        )

        createCardView(texts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createCardView(texts: Array<String>) {
        for (i in texts.indices) {
            val cardIndex = i // Идентификатор карточки

            // Создание макета для карточки и ее содержимого
            val cardContainer = LinearLayout(context)
            val cardContainerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardContainerParams.setMargins(22, -3, 22, 0)
            cardContainer.layoutParams = cardContainerParams

            val iconState = booleanArrayOf(true)

            // Создание карточки
            val cardView = CardView(requireContext())
            val cardParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardView.layoutParams = cardParams
            cardView.useCompatPadding = true;
            cardView.setCardBackgroundColor(ResourcesCompat.getColor(resources, android.R.color.white, null))
            cardView.cardElevation = 10f
            cardView.radius = 32f

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

            // Создание вложенного макета для текста
            val nestedFrameLayout = LinearLayout(requireContext())
            val nestedParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            nestedParams.setMargins(25, 29, 25, 15)
            nestedFrameLayout.layoutParams = nestedParams
            nestedFrameLayout.setBackgroundResource(R.drawable.one_more_corner_back)
            nestedFrameLayout.visibility = View.GONE
            val nestedTextView = TextView(requireContext())
            val nestedTextParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            nestedTextView.textSize = 15f
            nestedTextParams.setMargins(55, 175, 55, 45)
            nestedTextView.layoutParams = nestedTextParams
            nestedTextView.text = "Ваш текст здесь"

            // Установка параметра междустрочного интервала
            val lineSpacingExtra = 8f // Пример значения междустрочного интервала
            nestedTextView.setLineSpacing(lineSpacingExtra, 1.0f) // 1.0f - масштаб междустрочного интервала

            nestedFrameLayout.addView(nestedTextView)

            // Добавление всех элементов во вложенный макет
            innerLayout.addView(textView)
            innerLayout.addView(plusIcon)
            cardView.addView(innerLayout)
            cardContainer.addView(cardView) // Добавление карточки в контейнер
            cardContainer.addView(nestedFrameLayout) // Добавление вложенного макета в контейнер карточки
            faqCardHolder.addView(cardContainer) // Добавление контейнера карточки в общий контейнер

            // Обработчик нажатия на иконку
            plusIcon.setOnClickListener {
                if (iconState[0]) {
                    nestedFrameLayout.visibility = View.VISIBLE
                    plusIcon.setImageResource(R.drawable.upload)
                    iconState[0] = false
                } else {
                    nestedFrameLayout.visibility = View.GONE
                    plusIcon.setImageResource(R.drawable.down_up)
                    iconState[0] = true
                }
            }
        }
    }
}
