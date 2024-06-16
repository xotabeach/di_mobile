package com.example.myapplication.doctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R



class DoctorAdapter(private val faqList: List<DoctorItem>) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)
        val toggleButton: ImageButton = itemView.findViewById(R.id.toggleButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentItem = faqList[position]
        holder.questionTextView.text = currentItem.question
        holder.answerTextView.text = currentItem.answer
        holder.answerTextView.visibility = if (currentItem.isExpanded) View.VISIBLE else View.GONE
        holder.toggleButton.setImageResource(if (currentItem.isExpanded) R.drawable.upload else R.drawable.down_arrow)

        holder.questionTextView.setOnClickListener {
            currentItem.isExpanded = !currentItem.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = faqList.size
}
