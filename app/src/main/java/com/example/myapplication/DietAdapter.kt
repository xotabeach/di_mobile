package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

data class DietDay(
    val day: String,
    val breakfast: List<String>,
    val lunch: List<String>,
    val dinner: List<String>
)

class DietAdapter(private val dietDays: List<DietDay>) : RecyclerView.Adapter<DietAdapter.DietViewHolder>() {

    inner class DietViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
        val breakfastTextView: TextView = itemView.findViewById(R.id.breakfastTextView)
        val lunchTextView: TextView = itemView.findViewById(R.id.lunchTextView)
        val dinnerTextView: TextView = itemView.findViewById(R.id.dinnerTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diet_day, parent, false)
        return DietViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val currentDay = dietDays[position]
        holder.dayTextView.text = currentDay.day
        holder.breakfastTextView.text = "Завтрак:\n" + currentDay.breakfast.joinToString("\n")
        holder.lunchTextView.text = "Обед:\n" + currentDay.lunch.joinToString("\n")
        holder.dinnerTextView.text = "Ужин:\n" + currentDay.dinner.joinToString("\n")
    }

    override fun getItemCount() = dietDays.size
}
