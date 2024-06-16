package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DietFragment : Fragment() {

    private lateinit var diseaseSpinner: Spinner
    private lateinit var excludeProductsTextView: MultiAutoCompleteTextView
    private lateinit var createDietButton: Button
    private lateinit var recyclerViewDiet: RecyclerView

    private val openAIApi: OpenAIApi by lazy {
        OpenAIApi.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diet, container, false)
        diseaseSpinner = view.findViewById(R.id.spinnerDisease)
        excludeProductsTextView = view.findViewById(R.id.multiAutoCompleteTextView)
        createDietButton = view.findViewById(R.id.buttonCreateDiet)
        recyclerViewDiet = view.findViewById(R.id.recyclerViewDiet)

        val diseases = resources.getStringArray(R.array.diseases_array)
        val products = resources.getStringArray(R.array.products_array)

        val diseaseAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, diseases)
        diseaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        diseaseSpinner.adapter = diseaseAdapter

        val productAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, products)
        excludeProductsTextView.setAdapter(productAdapter)
        excludeProductsTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        createDietButton.setOnClickListener {
            val selectedDisease = diseaseSpinner.selectedItem.toString()
            val excludedProducts = excludeProductsTextView.text.toString()
            generateDiet(selectedDisease, excludedProducts)
        }

        recyclerViewDiet.layoutManager = LinearLayoutManager(context)
        return view
    }

    private fun generateDiet(disease: String, excludeProducts: String) {
        var prompt = ""

        if (excludeProducts.isNotEmpty()) {
            prompt = """
            Создай диету на 7 дней для человека с болезнью $disease, исключая следующие продукты: $excludeProducts.
            Формат вывода должен быть следующим:
            Понедельник:
            Завтрак:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Обед:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Ужин:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Вторник:
            Завтрак:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Обед:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Ужин:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            ...
            Убедитесь, что каждый день содержит три блюда на завтрак, обед и ужин.
        """.trimIndent()
        } else {
            prompt = """
            Создай диету на 7 дней для человека с болезнью $disease.
            Формат вывода должен быть следующим:
            Понедельник:
            Завтрак:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Обед:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Ужин:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Вторник:
            Завтрак:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Обед:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            Ужин:
            1) Блюдо 1
            2) Блюдо 2
            3) Блюдо 3
            ...
            Убедитесь, что каждый день содержит три блюда на завтрак, обед и ужин.
        """.trimIndent()
        }

        val request = OpenAIRequest(
            model = "gpt-3.5-turbo",
            prompt = prompt,
            max_tokens = 300
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = openAIApi.generateDiet(request)
                val dietText = response.choices.first().text.trim()
                val dietDays = parseDietResponse(dietText)
                withContext(Dispatchers.Main) {
                    recyclerViewDiet.adapter = DietAdapter(dietDays)
                    recyclerViewDiet.visibility = View.VISIBLE
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    handleHttpException(e)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Произошла ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun parseDietResponse(response: String): List<DietDay> {
        val dietDays = mutableListOf<DietDay>()
        val days = response.split("\n\n")

        for (day in days) {
            val lines = day.split("\n")
            val dayName = lines[0]
            val breakfast = mutableListOf<String>()
            val lunch = mutableListOf<String>()
            val dinner = mutableListOf<String>()

            var currentMeal = ""
            for (line in lines.drop(1)) {
                when {
                    line.startsWith("Завтрак:") -> {
                        currentMeal = "Завтрак"
                    }
                    line.startsWith("Обед:") -> {
                        currentMeal = "Обед"
                    }
                    line.startsWith("Ужин:") -> {
                        currentMeal = "Ужин"
                    }
                    else -> {
                        when (currentMeal) {
                            "Завтрак" -> breakfast.add(line.trim())
                            "Обед" -> lunch.add(line.trim())
                            "Ужин" -> dinner.add(line.trim())
                        }
                    }
                }
            }

            dietDays.add(DietDay(dayName, breakfast, lunch, dinner))
        }

        return dietDays
    }

    private fun handleHttpException(e: HttpException) {
        when (e.code()) {
            401 -> {
                Toast.makeText(requireContext(), "API ключ неверный или отсутствует.", Toast.LENGTH_LONG).show()
            }
            429 -> {
                Toast.makeText(requireContext(), "Превышена квота API. Пожалуйста, проверьте ваш план и квоту.", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Произошла ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
