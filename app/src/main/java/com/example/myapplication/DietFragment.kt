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

    // Predefined diet plans
    private val dietMap = mapOf(
        "Гастрэктомия" to """
        Понедельник:
        Завтрак:
        1) Овсяная каша на воде
        2) Вареное яйцо
        3) Компот из сухофруктов
        Обед:
        1) Овощной суп
        2) Паровая куриная котлета
        3) Пюре из кабачков
        Ужин:
        1) Рисовая каша
        2) Рыба на пару
        3) Чай с ромашкой
        
        Вторник:
        Завтрак:
        1) Гречневая каша
        2) Йогурт без добавок
        3) Сухофрукты
        Обед:
        1) Куриный бульон с вермишелью
        2) Отварная индейка
        3) Тушеная морковь
        Ужин:
        1) Творог
        2) Овощное пюре
        3) Чай с мятой
        
        ...
    """.trimIndent(),
        "Целиакия" to """
        Понедельник:
        Завтрак:
        1) Гречневая каша с ягодами
        2) Яйцо всмятку
        3) Чай без сахара
        Обед:
        1) Тыквенный суп
        2) Куриная грудка на пару
        3) Салат из свежих овощей
        Ужин:
        1) Рис с овощами
        2) Паровые котлеты из индейки
        3) Травяной чай
        
        Вторник:
        Завтрак:
        1) Овсяная каша без глютена
        2) Натуральный йогурт
        3) Яблоко
        Обед:
        1) Борщ без глютена
        2) Запеченная рыба
        3) Винегрет
        Ужин:
        1) Картофельное пюре
        2) Овощное рагу
        3) Чай с лимоном
        
        ...
    """.trimIndent(),
        "Гастрит обострение" to """
        Понедельник:
        Завтрак:
        1) Манная каша
        2) Паровой омлет
        3) Кисель
        Обед:
        1) Слизистый суп
        2) Тефтели на пару
        3) Тушеные кабачки
        Ужин:
        1) Рисовый пудинг
        2) Вареная рыба
        3) Чай с ромашкой
        
        Вторник:
        Завтрак:
        1) Геркулесовая каша
        2) Творожная запеканка
        3) Компот из сухофруктов
        Обед:
        1) Суп-пюре из овощей
        2) Отварная курица
        3) Пюре из цветной капусты
        Ужин:
        1) Творог
        2) Овощное рагу
        3) Травяной чай
        
        ...
    """.trimIndent(),
        "Резекция желудка" to """
        Понедельник:
        Завтрак:
        1) Рисовая каша на воде
        2) Вареное яйцо
        3) Чай с медом
        Обед:
        1) Суп из овощей
        2) Паровая куриная котлета
        3) Тушеные овощи
        Ужин:
        1) Гречневая каша
        2) Рыба на пару
        3) Травяной чай
        
        Вторник:
        Завтрак:
        1) Овсяная каша на воде
        2) Йогурт
        3) Яблоко
        Обед:
        1) Куриный бульон
        2) Отварная говядина
        3) Салат из овощей
        Ужин:
        1) Творожная запеканка
        2) Овощное пюре
        3) Чай с мятой
        
        ...
    """.trimIndent(),
        "Панкреатит ремиссия" to """
        Понедельник:
        Завтрак:
        1) Овсяная каша
        2) Яйцо всмятку
        3) Компот из сухофруктов
        Обед:
        1) Овощной суп
        2) Куриная грудка на пару
        3) Пюре из моркови
        Ужин:
        1) Рисовая каша
        2) Тушеная рыба
        3) Чай с мятой
        
        Вторник:
        Завтрак:
        1) Гречневая каша
        2) Натуральный йогурт
        3) Груша
        Обед:
        1) Тыквенный суп
        2) Тефтели на пару
        3) Тушеные овощи
        Ужин:
        1) Творог
        2) Овощное рагу
        3) Чай с ромашкой
        
        ...
    """.trimIndent(),
        "Гастрит (ремиссия)" to """
        Понедельник:
        Завтрак:
        1) Овсяная каша
        2) Паровой омлет
        3) Компот из яблок
        Обед:
        1) Овощной суп
        2) Отварная курица
        3) Пюре из картофеля
        Ужин:
        1) Рис с овощами
        2) Запеченная рыба
        3) Травяной чай
        
        Вторник:
        Завтрак:
        1) Гречневая каша
        2) Йогурт без добавок
        3) Яблоко
        Обед:
        1) Борщ
        2) Куриные котлеты на пару
        3) Салат из свежих овощей
        Ужин:
        1) Творог
        2) Овощное рагу
        3) Чай с лимоном
        
        ...
    """.trimIndent(),
        "ГЭРБ" to """
        Понедельник:
        Завтрак:
        1) Геркулесовая каша
        2) Вареное яйцо
        3) Чай с ромашкой
        Обед:
        1) Куриный бульон с вермишелью
        2) Куриная грудка на пару
        3) Тушеная морковь
        Ужин:
        1) Творог
        2) Овощное пюре
        3) Чай с мятой
        
        Вторник:
        Завтрак:
        1) Овсяная каша
        2) Натуральный йогурт
        3) Яблоко
        Обед:
        1) Тыквенный суп
        2) Запеченная рыба
        3) Салат из свежих овощей
        Ужин:
        1) Гречневая каша
        2) Овощное рагу
        3) Чай с лимоном
        
        ...
    """.trimIndent(),
        "Болезнь Крона" to """
        Понедельник:
        Завтрак:
        1) Овсяная каша на воде
        2) Вареное яйцо
        3) Компот из сухофруктов
        Обед:
        1) Овощной суп
        2) Паровая куриная котлета
        3) Пюре из кабачков
        Ужин:
        1) Рисовая каша
        2) Рыба на пару
        3) Чай с ромашкой
        
        Вторник:
        Завтрак:
        1) Гречневая каша
        2) Йогурт без добавок
        3) Сухофрукты
        Обед:
        1) Куриный бульон с вермишелью
        2) Отварная индейка
        3) Тушеная морковь
        Ужин:
        1) Творог
         2) Овощное пюре
        3) Чай с мятой
        
        ...
    """.trimIndent(),
        "Холецистит" to """
        Понедельник:
        Завтрак:
        1) Овсяная каша на воде
        2) Вареное яйцо
        3) Компот из сухофруктов
        Обед:
        1) Овощной суп
        2) Паровая куриная котлета
        3) Пюре из кабачков
        Ужин:
        1) Рисовая каша
        2) Рыба на пару
        3) Чай с ромашкой
        
        Вторник:
        Завтрак:
        1) Гречневая каша
        2) Йогурт без добавок
        3) Сухофрукты
        Обед:
        1) Куриный бульон с вермишелью
        2) Отварная индейка
        3) Тушеная морковь
        Ужин:
        1) Творог
        2) Овощное пюре
        3) Чай с мятой
        
        ...
    """.trimIndent()
    )


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
                    handleHttpException(e, disease)
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

    private fun handleHttpException(e: HttpException, disease: String) {
        val localDiet = dietMap[disease]
        if (localDiet != null) {
            val dietDays = parseDietResponse(localDiet)
            recyclerViewDiet.adapter = DietAdapter(dietDays)
            recyclerViewDiet.visibility = View.VISIBLE
        } else {
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
}
