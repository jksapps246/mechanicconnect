package com.jksapps.mechanicconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater

class UserSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)
        //custom image in action bar
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_bar_logo, null)
        actionBar.customView = view
    }
}