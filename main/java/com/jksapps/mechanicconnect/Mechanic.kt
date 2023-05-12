package com.jksapps.mechanicconnect

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.tabs.TabLayout



class Mechanic : AppCompatActivity() {

    public lateinit var userId: String
    public lateinit var dB: DBHelper
    public lateinit var aDetails: ContentValues
    public lateinit var name: String
    public lateinit var address: String
    public lateinit var eAddress: TextView
    public var ids: Array<String> = arrayOf()
    public var names: Array<String> = arrayOf()
    public var businessNames: Array<String> = arrayOf()
    public var addresses: Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanic)
        //get address for map
        userId = intent.getStringExtra("userId").toString()
        dB = DBHelper(this)
        aDetails = dB.getUserAddress(userId)
        name = aDetails.getAsString("name")
        address = aDetails.getAsString("address")
        eAddress = findViewById<TextView>(R.id.editAddressMechanic)
        //add address to edit text
        eAddress.text = address

        //custom image in action bar
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_bar_logo, null)
        actionBar.customView = view
        //set up tabs
        setUpTabBar()
        //set up mechanic addresses
        setUpAddresses()
    }

    private fun setUpTabBar() {
        val tabLayout = findViewById<TabLayout>(R.id.mechanic_tab_layout)
        val adapter = TabPageAdapterMechanic(this, tabLayout.tabCount)
        val viewpager = findViewById<ViewPager2>(R.id.mechanic_view)
        viewpager.adapter = adapter

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setUpAddresses() {
        val dB = DBHelper(this)
        val cursor = dB.getMechanics()
        var col: Int
        var data: String
        try {
            cursor!!.moveToFirst()
            do {
                //get name from database
                col = cursor.getColumnIndex("name")
                data = cursor.getString(col)
                //add name reference to list
                names += data

                //get business name from database
                col = cursor.getColumnIndex("business_name")
                data = cursor.getString(col)
                //add name reference to list
                businessNames += data

                //get address from database
                col = cursor.getColumnIndex("address")
                data = cursor.getString(col)
                //add address to list
                addresses += data

                //get userid from database
                col = cursor.getColumnIndex("id")
                data = cursor.getString(col)
                //add userid to list
                ids += data

            } while (cursor!!.moveToNext())
            dB.close()
        } catch (e: Exception) {
            Toast.makeText(this, e.message + " Mech", Toast.LENGTH_SHORT).show()
        }
    }
/*
    fun searchMap(v: View) {
        val searchAddress = findViewById<EditText>(R.id.editAddressMechanic)
        if (searchAddress.text.isNotEmpty()) {
            val add = FragmentMechanicMaps()
            val str = add.addMyMarker(searchAddress.text.toString())

            searchAddress?.setText(str)
        }
        else {
            searchAddress.error = "Enter Address of Postal Code"
        }
    }
    */

}
