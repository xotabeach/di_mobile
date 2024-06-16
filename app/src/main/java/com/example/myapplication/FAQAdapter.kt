import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class FAQAdapter(private val faqList: List<FAQItem>) : RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faqItem = faqList[position]
        holder.bind(faqItem)
    }

    override fun getItemCount(): Int = faqList.size

    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

        fun bind(faqItem: FAQItem) {
            titleTextView.text = faqItem.title
            contentTextView.text = faqItem.content
            contentTextView.visibility = if (faqItem.isExpanded) View.VISIBLE else View.GONE

            titleTextView.setOnClickListener {
                faqItem.isExpanded = !faqItem.isExpanded
                notifyItemChanged(adapterPosition)
            }
        }
    }
}
