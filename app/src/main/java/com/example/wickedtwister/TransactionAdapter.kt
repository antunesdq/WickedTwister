package com.example.wickedtwister

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter (private val dataSet: List<Transaction>, private val accounts:MutableMap<String, Account>) :
        RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagNameTv: TextView = view.findViewById(R.id.tra_card_tag_name)
        val traValueTv: TextView = view.findViewById(R.id.tra_card_tra_value)
        val traNameTv: TextView = view.findViewById(R.id.tra_card_tra_name)
        val traDateTv: TextView = view.findViewById(R.id.tra_card_tra_date)
        val accAliasTv: TextView = view.findViewById(R.id.tra_card_acc_alias)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.transaction_card, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tagNameTv.text = dataSet[position].tagName
        viewHolder.traDateTv.text = dataSet[position].traDate
        viewHolder.traNameTv.text = dataSet[position].traName
        viewHolder.traValueTv.text = dataSet[position].traValue
        viewHolder.accAliasTv.text = accounts[dataSet[position].accId]?.accAlias.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}