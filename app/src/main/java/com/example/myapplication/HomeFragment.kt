package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarks: MutableSet<String>

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val constraintLayout = binding.mainConstraintLayout

        val cardContainer = ConstraintLayout(context).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToBottom = R.id.textDescr
                startToStart = constraintLayout.id
                endToEnd = constraintLayout.id
                topMargin = 20.dpToPx(context)
            }
        }

        constraintLayout.addView(cardContainer)

        val cardViews = listOf(
            createCardView(context, R.drawable.pic1, "Гастрэктомия"),
            createCardView(context, R.drawable.pic2, "Целиакия"),
            createCardView(context, R.drawable.pic3, "Гастрит обострение"),
            createCardView(context, R.drawable.pic4, "Резекция желудка"),
            createCardView(context, R.drawable.pic5, "Панкреатит ремиссия"),
            createCardView(context, R.drawable.pic6, "Гастрит(ремиссия)"),
            createCardView(context, R.drawable.pic7, "ГЭРБ"),
            createCardView(context, R.drawable.pic8, "Болезнь Крона"),
            createCardView(context, R.drawable.pic9, "Холецистит")
        )

        val firstRow = createRow(context, cardViews.subList(0, 3))
        val secondRow = createRow(context, cardViews.subList(3, 6))
        val thirdRow = createRow(context, cardViews.subList(6, 9))

        cardContainer.addView(firstRow)
        cardContainer.addView(secondRow)
        cardContainer.addView(thirdRow)

        val constraintSet = ConstraintSet().apply {
            clone(constraintLayout)

            connect(R.id.textDescr, ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP, 65.dpToPx(context))
            connect(R.id.textDescr, ConstraintSet.START, constraintLayout.id, ConstraintSet.START)
            connect(R.id.textDescr, ConstraintSet.END, constraintLayout.id, ConstraintSet.END)

            connect(cardContainer.id, ConstraintSet.TOP, R.id.textDescr, ConstraintSet.BOTTOM, 16.dpToPx(context))
            connect(cardContainer.id, ConstraintSet.START, constraintLayout.id, ConstraintSet.START)
            connect(cardContainer.id, ConstraintSet.END, constraintLayout.id, ConstraintSet.END)
        }

        constraintSet.applyTo(constraintLayout)

        val cardContainerSet = ConstraintSet().apply {
            clone(cardContainer)

            connect(firstRow.id, ConstraintSet.TOP, cardContainer.id, ConstraintSet.TOP, 16.dpToPx(context))
            connect(firstRow.id, ConstraintSet.START, cardContainer.id, ConstraintSet.START)
            connect(firstRow.id, ConstraintSet.END, cardContainer.id, ConstraintSet.END)

            connect(secondRow.id, ConstraintSet.TOP, firstRow.id, ConstraintSet.BOTTOM, 16.dpToPx(context))
            connect(secondRow.id, ConstraintSet.START, cardContainer.id, ConstraintSet.START)
            connect(secondRow.id, ConstraintSet.END, cardContainer.id, ConstraintSet.END)

            connect(thirdRow.id, ConstraintSet.TOP, secondRow.id, ConstraintSet.BOTTOM, 16.dpToPx(context))
            connect(thirdRow.id, ConstraintSet.START, cardContainer.id, ConstraintSet.START)
            connect(thirdRow.id, ConstraintSet.END, cardContainer.id, ConstraintSet.END)
        }

        cardContainerSet.applyTo(cardContainer)

        cardViews.forEach { cardView ->
            cardView.setOnClickListener {
                val cardText = (cardView.getChildAt(0) as LinearLayout).getChildAt(1) as TextView
                val bundle = Bundle().apply {
                    putString("title", cardText.text.toString())
                }
                findNavController().navigate(R.id.moreInfoFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun createRow(context: Context, cardViews: List<CardView>): ConstraintLayout {
        val rowLayout = ConstraintLayout(context).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16.dpToPx(context)
            }
        }

        cardViews.forEachIndexed { index, cardView ->
            cardView.id = View.generateViewId()
            rowLayout.addView(cardView)

            val set = ConstraintSet()
            set.clone(rowLayout)

            if (index == 0) {
                set.connect(cardView.id, ConstraintSet.START, rowLayout.id, ConstraintSet.START, 8.dpToPx(context))
            } else {
                set.connect(cardView.id, ConstraintSet.START, cardViews[index - 1].id, ConstraintSet.END, 6.dpToPx(context))
            }

            if (index == cardViews.size - 1) {
                set.connect(cardView.id, ConstraintSet.END, rowLayout.id, ConstraintSet.END, 2.dpToPx(context))
            }

            set.applyTo(rowLayout)
        }

        return rowLayout
    }

    private fun createCardView(context: Context, imageResId: Int, cardText: String): CardView {
        val cardView = CardView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                width = 130.dpToPx(context)
                height = 130.dpToPx(context)
            }
            radius = 10f
            cardElevation = 2f
        }

        val linearLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            setPadding(15.dpToPx(context), 15.dpToPx(context), 15.dpToPx(context), 15.dpToPx(context))
        }

        val imageView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 85.dpToPx(context))
            setImageResource(imageResId)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            text = cardText
            gravity = Gravity.CENTER
            typeface = ResourcesCompat.getFont(context, R.font.amatic_sc)
            textSize = 14f
        }

        val bookmarkImageView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(30.dpToPx(context), 30.dpToPx(context)).apply {
                gravity = Gravity.TOP or Gravity.END
            }
            setImageResource(if (bookmarks.contains(cardText)) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark_unselected)
            setOnClickListener {
                val isBookmarked = bookmarks.contains(cardText)
                if (isBookmarked) {
                    bookmarks.remove(cardText)
                    setImageResource(R.drawable.ic_bookmark_unselected)

                } else {
                    bookmarks.add(cardText)
                    setImageResource(R.drawable.ic_bookmark_selected)

                }
            }
        }

        val bookmarkContainer = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.TOP or Gravity.END
            addView(bookmarkImageView)
        }

        linearLayout.addView(imageView)
        linearLayout.addView(textView)
        cardView.addView(linearLayout)
        cardView.addView(bookmarkContainer)

        return cardView
    }
}
