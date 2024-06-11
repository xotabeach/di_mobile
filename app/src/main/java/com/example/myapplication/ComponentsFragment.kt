package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.DatabaseHelper
import com.example.myapplication.data.model.Component
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComponentsFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



            val view = inflater.inflate(R.layout.fragment_components, container, false)
        val componentsContainer = view.findViewById<LinearLayout>(R.id.components_container)


        val title = arguments?.getString("title");
        Log.d("title", "title is : $title")
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.moreInfoFragment, bundle)
        }


        lifecycleScope.launch {
            val components = withContext(Dispatchers.IO) {
                dbHelper.getComponentsByDiet("любая") // Замените "любая" на нужный параметр диеты
            }
            displayComponents(components, componentsContainer)
        }

        return view
    }

    private fun displayComponents(components: List<Component>, container: LinearLayout) {
        for (component in components) {
            val componentView = layoutInflater.inflate(R.layout.component_item, container, false) as CardView

            val componentName = componentView.findViewById<TextView>(R.id.component_name)
            val componentENumber = componentView.findViewById<TextView>(R.id.component_e_number)

            componentName.text = component.name
            componentENumber.text = component.eNumber

            val backgroundColor = when (component.dangerLevel) {
                1 -> Color.BLACK
                2 -> Color.GRAY
                3 -> Color.WHITE
                else -> Color.TRANSPARENT
            }
            componentView.setCardBackgroundColor(backgroundColor)

            // Устанавливаем цвет текста в зависимости от цвета фона карточки
            val textColor = if (backgroundColor == Color.BLACK) {
                Color.WHITE
            } else {
                Color.BLACK
            }
            componentName.setTextColor(textColor)
            componentENumber.setTextColor(textColor)

            container.addView(componentView)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ComponentsFragment()
    }
}
