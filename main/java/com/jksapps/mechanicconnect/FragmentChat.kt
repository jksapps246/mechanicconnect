package com.jksapps.mechanicconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

class FragmentChat : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }
    fun sendMsg(v: View){
        val userId = activity?.intent?.getStringExtra("userId").toString()
        val dB = DBHelper(context)
        var message = v.findViewById<EditText>(R.id.sendMessage)
        if(message.text.isNotEmpty()){
            sendMessage(userId,dB.getMechanicUser(userId).toString(),message.text.toString())
        }
        dB.close()
    }
    private fun sendMessage(senderId: String, receiverId: String, message: String){
        val dB = DBHelper(context)
        try{
            dB.insertMessage(senderId, receiverId, message)

        }catch (e: Exception){
            Toast.makeText(context,e.message + " FChat",Toast.LENGTH_SHORT).show()
        }
    }
    private fun readMessage(senderId: String, receiverId: String){

    }
}