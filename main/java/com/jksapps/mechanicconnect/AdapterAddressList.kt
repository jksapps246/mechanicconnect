package com.jksapps.mechanicconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterAddressList(private val mechanicList: ArrayList<DataAddressMarkers>) :
    RecyclerView.Adapter<AdapterAddressList.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    var onItemClick : ((DataAddressMarkers) -> Unit)? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_mechanic_list, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = mechanicList[position]
        viewHolder.name.text = currentItem.name
        viewHolder.businessName.text = currentItem.businessName
        viewHolder.id.text = currentItem.id
        viewHolder.address.text = currentItem.address

        viewHolder.itemView.setOnClickListener { v ->
            onItemClick?.invoke(currentItem)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mechanicList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val businessName: TextView
        val id: TextView
        val address: TextView

        init {
            // Define click listener for the ViewHolder's View
            name = view.findViewById(R.id.MlistName)
            businessName = view.findViewById(R.id.MlistBusiness)
            id = view.findViewById(R.id.MlistId)
            address = view.findViewById(R.id.MlistAddress)
        }
    }

}
