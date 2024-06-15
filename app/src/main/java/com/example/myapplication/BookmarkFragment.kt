package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarks: MutableSet<String>

    private lateinit var bookmarkContainer: LinearLayout

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
        bookmarkContainer = view.findViewById(R.id.bookmarks_container)

        // Вывод в консоль
        bookmarks.forEach {
            println("Bookmark: $it")
        }

        displayBookmarks(bookmarks, bookmarkContainer)
    }

    private fun displayBookmarks(bookmarks: MutableSet<String>, container: LinearLayout) {
        container.removeAllViews()
        for (product in bookmarks) {
            val productView = layoutInflater.inflate(R.layout.bookmark_item, container, false) as CardView

            val bookmarksName = productView.findViewById<TextView>(R.id.bookmark_name)
            bookmarksName.text = product

            val deleteIcon = productView.findViewById<ImageView>(R.id.bookmark_delete_icon)
            deleteIcon.setOnClickListener {
                val animation = TranslateAnimation(0f, -productView.width.toFloat(), 0f, 0f).apply {
                    duration = 300
                    fillAfter = true
                    setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}
                        override fun onAnimationEnd(animation: Animation) {
                            bookmarks.remove(product)
                            container.removeView(productView)
                        }
                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                }
                productView.startAnimation(animation)
            }

            container.addView(productView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
