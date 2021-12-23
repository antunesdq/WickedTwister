package com.example.wickedtwister

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionRViewAdaptor(private val dataset: MutableMap<String, Transaction>) :
    RecyclerView.Adapter<TransactionRViewAdaptor.MyViewHolder>() {

    class MyViewHolder(view: View, clickAtPosition:(Int) ->Unit) : RecyclerView.ViewHolder(view) {
        val itemnameTV: TextView = view.findViewById(R.id.textView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = MyViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.transaction_card, viewGroup, false)){
        }
        return view
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val data = dataset.values.toList()
        viewHolder.itemnameTV.text = data[position].traId
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size

}

