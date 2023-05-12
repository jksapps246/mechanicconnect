package com.jksapps.mechanicconnect

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast

class Contact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        //custom image in action bar
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_bar_logo, null)
        actionBar.customView = view

        val userId = intent.getStringExtra("userId")
        loadEmailName(userId.toString())
    }
    private fun loadEmailName(userId: String){
        val dB = DBHelper(this)
        val cDetails = dB.getContactDetails(userId)
        val nameTxt = findViewById<EditText>(R.id.editName)
        val emailTxt = findViewById<EditText>(R.id.editEmail)

        nameTxt.setText(cDetails.getAsString(DBHelper.ACCOUNT_FIRST_NAME_COL))
        emailTxt.setText(cDetails.getAsString(DBHelper.ACCOUNT_EMAIL_COL))
    }
    fun sendMsg(v: View) {
        val nameTxt = findViewById<EditText>(R.id.editName)
        val emailTxt = findViewById<EditText>(R.id.editEmail)
        val subTxt = findViewById<EditText>(R.id.editSubject)
        val descTxt = findViewById<EditText>(R.id.editDesc)

        if (nameTxt.text.isEmpty()) {
            //Toast.makeText(this, "Warning! Missing Name", Toast.LENGTH_LONG).show()
            nameTxt.error = "Please Enter Name"
        }
        else if (emailTxt.text.isEmpty()) {
            //Toast.makeText(this, "Warning! Missing Email", Toast.LENGTH_LONG).show()
            emailTxt.error = "Please Enter Email"
        }
        else if (subTxt.text.isEmpty()) {
            //Toast.makeText(this, "Warning! Missing Subject", Toast.LENGTH_LONG).show()
            subTxt.error = "Please Enter Subject"
        }
        else if (descTxt.text.isEmpty()) {
            //Toast.makeText(this, "Warning! Missing Description", Toast.LENGTH_LONG).show()
            descTxt.error = "Please Enter Description"
        }
        else {
            val email = Intent(Intent.ACTION_SEND)
            email.data = Uri.parse("mailto:")
            email.type = "text/plain"
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf("jksapps246@gmail.com"))
            email.putExtra(Intent.EXTRA_SUBJECT, "RE: "+ subTxt.text.toString())
            email.putExtra(Intent.EXTRA_TEXT, "From: " + nameTxt.text.toString() + " \n\n Email: " + emailTxt.text.toString() + " \n\n Description: \n - " + descTxt.text.toString())
            try {
                startActivity(email)
            }
            catch ( e: Exception) {

                Toast.makeText(this, e.message + "check Contact", Toast.LENGTH_SHORT).show()
            }
        }

    }
}