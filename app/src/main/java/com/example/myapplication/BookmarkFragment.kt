package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarks: MutableSet<String>

    private lateinit var bookmark_container: LinearLayout
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            bookmarks = context.bookmarks
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookmark_container = view.findViewById(R.id.bookmarks_container)

        //println("bookmark 1 ${bookmarks.elementAt(1)}")
        // Вывод в консоль
        bookmarks.forEach {
            println("Bookmark: $it")
        }

        displayBookmarks(bookmarks, bookmark_container)
        // Отображение в текстовом поле
        //binding.textViewBookmarks.text = bookmarks.joinToString("\n")
    }

    private fun displayBookmarks(bookmarks: MutableSet<String>, container: LinearLayout) {
        for (product in bookmarks) {
            val productView = layoutInflater.inflate(R.layout.bookmark_item, container, false) as CardView

            val bookmarksName = productView.findViewById<TextView>(R.id.bookmark_name)


            bookmarksName.text = product



            container.addView(productView)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
