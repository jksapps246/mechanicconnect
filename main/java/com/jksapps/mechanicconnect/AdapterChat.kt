package com.jksapps.mechanicconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterChat(private val dataChatList: ArrayList<DataChat>) :
    RecyclerView.Adapter<AdapterChat.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        if(viewType == MESSAGE_TYPE_RIGHT){
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_message_right, viewGroup, false)
            return ViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_message_left, viewGroup, false)
            return ViewHolder(view)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = dataChatList[position]
        viewHolder.message.text = currentItem.message
        viewHolder.timeStamp.text = currentItem.timeStamp
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataChatList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message = view.findViewById<TextView>(R.id.msgFromUser)
        val timeStamp = view.findViewById<TextView>(R.id.msgTimeFromUser)

    }

    override fun getItemViewType(position: Int): Int {

            //database check for sender and receiver using the user and mechanic ids
            if(dataChatList[position].senderId != "hi"){
                return MESSAGE_TYPE_RIGHT
            }else {
                return MESSAGE_TYPE_LEFT
            }

    }
}
