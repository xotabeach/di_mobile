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
import com.example.myapplication.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_products, container, false)
        val productsContainer = view.findViewById<LinearLayout>(R.id.products_container)

        val title = arguments?.getString("title");
        Log.d("title", "title is : $title")
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val bundle = Bundle().apply { putString("title", title) }
            findNavController().navigate(R.id.moreInfoFragment, bundle)
        }

        lifecycleScope.launch {
            val products = withContext(Dispatchers.IO) {
                dbHelper.getProductsByDiet("любая") // Замените "some_diet" на нужный параметр диеты
            }
            displayProducts(products, productsContainer)
        }

        return view
    }

    private fun displayProducts(products: List<Product>, container: LinearLayout) {
        for (product in products) {
            val productView = layoutInflater.inflate(R.layout.product_item, container, false) as CardView

            val productName = productView.findViewById<TextView>(R.id.product_name)
            val productUsefulness = productView.findViewById<TextView>(R.id.product_usefulness)

            productName.text = product.name
            productUsefulness.text = product.usefulness.toString()

            val backgroundColor = when {
                product.usefulness in -4..0 -> Color.YELLOW
                product.usefulness <= -5 -> Color.RED
                product.usefulness in 1..20 -> Color.GREEN
                else -> Color.WHITE
            }
            productView.setCardBackgroundColor(backgroundColor)

            container.addView(productView)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductsFragment()
    }
}
