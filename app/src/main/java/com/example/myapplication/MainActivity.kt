package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.ui.login.LoginActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запускаем LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))

        setContentView(R.layout.activity_main)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainConstraintLayout)

        val textName = findViewById<TextView>(R.id.textDescr)

        val cardContainer = ConstraintLayout(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToBottom = R.id.textDescr
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topMargin = 20.dpToPx(this@MainActivity)
            }
        }

        constraintLayout.addView(cardContainer)

        val cardViews = listOf(
            createCardView(this, R.drawable.pic1, "Гастрэктомия"),
            createCardView(this, R.drawable.pic2, "Целиакия"),
            createCardView(this, R.drawable.pic3, "Гастрит обострение"),
            createCardView(this, R.drawable.pic4, "Резекция желудка"),
            createCardView(this, R.drawable.pic5, "Панкреатит ремиссия"),
            createCardView(this, R.drawable.pic6, "Гастрит(ремиссия)"),
            createCardView(this, R.drawable.pic7, "ГЭРБ"),
            createCardView(this, R.drawable.pic8, "Болезнь Крона"),
            createCardView(this, R.drawable.pic9, "Холецистит")
        )

        val firstRow = createRow(this, cardViews.subList(0, 3))
        val secondRow = createRow(this, cardViews.subList(3, 6))
        val thirdRow = createRow(this, cardViews.subList(6, 9))

        cardContainer.addView(firstRow)
        cardContainer.addView(secondRow)
        cardContainer.addView(thirdRow)

        val constraintSet = ConstraintSet().apply {
            clone(constraintLayout)

            connect(R.id.textDescr, ConstraintSet.TOP, R.id.textApp, ConstraintSet.BOTTOM, 20.dpToPx(this@MainActivity))
            connect(R.id.textDescr, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(R.id.textDescr, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            connect(cardContainer.id, ConstraintSet.TOP, R.id.textDescr, ConstraintSet.BOTTOM, 16.dpToPx(this@MainActivity))
            connect(cardContainer.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(cardContainer.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        }

        constraintSet.applyTo(constraintLayout)

        val cardContainerSet = ConstraintSet().apply {
            clone(cardContainer)

            connect(firstRow.id, ConstraintSet.TOP, cardContainer.id, ConstraintSet.TOP, 16.dpToPx(this@MainActivity))
            connect(firstRow.id, ConstraintSet.START, cardContainer.id, ConstraintSet.START)
            connect(firstRow.id, ConstraintSet.END, cardContainer.id, ConstraintSet.END)

            connect(secondRow.id, ConstraintSet.TOP, firstRow.id, ConstraintSet.BOTTOM, 16.dpToPx(this@MainActivity))
            connect(secondRow.id, ConstraintSet.START, cardContainer.id, ConstraintSet.START)
            connect(secondRow.id, ConstraintSet.END, cardContainer.id, ConstraintSet.END)

            connect(thirdRow.id, ConstraintSet.TOP, secondRow.id, ConstraintSet.BOTTOM, 16.dpToPx(this@MainActivity))
            connect(thirdRow.id, ConstraintSet.START, cardContainer.id, ConstraintSet.START)
            connect(thirdRow.id, ConstraintSet.END, cardContainer.id, ConstraintSet.END)
        }

        cardContainerSet.applyTo(cardContainer)
    }

    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun createRow(context: Context, cardViews: List<CardView>): ConstraintLayout {
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
                set.connect(cardView.id, ConstraintSet.START, cardViews[index - 1].id, ConstraintSet.END, 8.dpToPx(context))
            }

            if (index == cardViews.size - 1) {
                set.connect(cardView.id, ConstraintSet.END, rowLayout.id, ConstraintSet.END, 8.dpToPx(context))
            }

            set.applyTo(rowLayout)
        }

        return rowLayout
    }

    fun createCardView(context: Context, imageResId: Int, cardText: String): CardView {
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

        linearLayout.addView(imageView)
        linearLayout.addView(textView)
        cardView.addView(linearLayout)

        return cardView
    }
}
