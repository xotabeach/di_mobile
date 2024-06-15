package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DietFragment : Fragment() {

    private lateinit var diseaseSpinner: Spinner
    private lateinit var excludeProductsTextView: MultiAutoCompleteTextView
    private lateinit var createDietButton: Button
    private lateinit var dietTextView: TextView

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
        dietTextView = view.findViewById(R.id.textViewDiet)

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

        return view
    }

    private fun generateDiet(disease: String, excludeProducts: String) {
        var prompt = ""

        if (excludeProducts.isNotEmpty()) {
            prompt = "Создай диету на 7 дней для болезни $disease, исключая продукты: $excludeProducts. Для каждого дня распредели всё на утро обед и ужин"
        } else prompt = "Создай диету на 7 дней для болезни $disease. Для каждого дня распредели всё на утро обед и ужин"

        val request = OpenAIRequest(
            model = "gpt-3.5-turbo",  // Указание модели
            prompt = prompt,
            max_tokens = 300
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = openAIApi.generateDiet(request)
                val diet = response.choices.first().text.trim()
                withContext(Dispatchers.Main) {
                    dietTextView.text = diet
                    dietTextView.visibility = View.VISIBLE
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
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
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Произошла ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
