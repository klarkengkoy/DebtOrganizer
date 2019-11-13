package com.csamson.debtorganizer.objects


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csamson.debtorganizer.R
import kotlinx.android.synthetic.main.row_layout.view.*


class ListAdapter(var listArray : ArrayList<EntryDetails>?, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = listArray!![position].name
        holder.amountWithCurrencyTextView.text = listArray!![position].amountWithCurrency
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false))
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return if (listArray?.size == 0 || listArray == null){
            0
        } else listArray!!.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val nameTextView: TextView = view.nameTextView
    val amountWithCurrencyTextView: TextView = view.amountWithCurrencyTextView
}
