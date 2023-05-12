package com.jksapps.mechanicconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterList(private val dataNotifyList: ArrayList<DataNotify>) :
    RecyclerView.Adapter<AdapterList.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    var onItemClick : ((DataNotify) -> Unit)? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_notify_list, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = dataNotifyList[position]
        viewHolder.image.setImageResource(currentItem.image)
        viewHolder.heading.text = currentItem.heading
        viewHolder.desc.text = currentItem.desc

        viewHolder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataNotifyList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heading: TextView
        val desc: TextView
        val image: ImageView

        init {
            // Define click listener for the ViewHolder's View
            heading = view.findViewById(R.id.listHeading)
            desc = view.findViewById(R.id.listDesc)
            image = view.findViewById(R.id.listImage)
        }
    }

}
