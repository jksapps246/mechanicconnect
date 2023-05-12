package com.jksapps.mechanicconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterService(private val serviceList: ArrayList<DataVehicleServices>) :
    RecyclerView.Adapter<AdapterService.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    var onItemClick : ((DataVehicleServices) -> Unit)? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_service_client, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = serviceList[position]
        viewHolder.sImage.setImageResource(currentItem.sImage)
        viewHolder.sName.text = currentItem.sName

        viewHolder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = serviceList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sName: TextView
        val sImage: ImageView

        init {
            // Define click listener for the ViewHolder's View
            sName = view.findViewById(R.id.vItemName)
            sImage = view.findViewById(R.id.vItemImage)
        }
    }

}
