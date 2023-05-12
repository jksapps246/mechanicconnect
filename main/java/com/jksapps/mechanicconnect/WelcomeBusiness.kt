package com.jksapps.mechanicconnect

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout


class WelcomeBusiness : AppCompatActivity() {
    private lateinit var userId : String
    private lateinit var welcome : String
    private var isMechanic : Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_business)
        //custom image in action bar
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_bar_logo, null)
        actionBar.customView = view

        welcome =  intent.getStringExtra("welcome").toString()
        userId =  intent.getStringExtra("userId").toString()
        isMechanic =  intent.getBooleanExtra("isMechanic",false)

        checkUpdates()
        setUpTabBar()
        val welcome = findViewById<TextView>(R.id.welcomeTxt)
        //val message =  intent.getStringExtra("welcome")
        welcome.text = ""//message.toString()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.personal_details -> gotoSetUp()
            R.id.user_settings -> gotoUserSettings()
            R.id.contact_support -> gotoContact()
            R.id.logout -> {
                //logout of account
                AlertDialog.Builder(this@WelcomeBusiness)
                    .setTitle("Exit Alert")
                    .setMessage("Do You Want To Logout?")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        exit()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->

                    }
                    .show()
            }
            else -> startActivity(Intent(this, Login::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpTabBar() {
        val tabLayout = findViewById<TabLayout>(R.id.tablayout)
        val adapter = TabPageAdapterBusiness(this, tabLayout.tabCount)
        val viewpager = findViewById<ViewPager2>(R.id.viewpagerB)
        viewpager.adapter = adapter

        viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun exit() {
        //clear any remember flags
        val db = DBHelper(this)
        db.clearRemember()
        db.close()
        val i = Intent(this, Login::class.java)
        // set the new task and clear flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }//exit()

    override fun onBackPressed()
    {
        AlertDialog.Builder(this)
            .setTitle("Exit Alert")
            .setMessage("Do You Want To Logout?")
            .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                exit()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->

            }
            .show()
    }
    private fun gotoSetUp(){
        val nextPage = Intent(this, Setup::class.java)
        nextPage.putExtra("welcome",welcome)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }
    private fun gotoAccountDetails(){
        val nextPage = Intent(this, DetailsAccount::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }
    private fun gotoPaymentDetails(){
       val nextPage = Intent(this, DetailsPayment::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }
    private fun gotoUserSettings(){
        val nextPage = Intent(this, UserSettings::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }
    private fun gotoContact(){
        val nextPage = Intent(this, Contact::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkUpdates(){
        //check for new clients
        val dB = DBHelper(this)
        //delete notifications of clients
        dB.deleteNotify(userId,"Client")
        val cursor = dB.getUsersMechanicToAccept(userId)
        if(cursor != null && cursor.count > 0) {
            //update with new client list
            cursor.moveToFirst()
            try {
                do{
                    //get userId(client) and userName from database
                    var col = cursor.getColumnIndex(DBHelper.USERS_USERNAME_COl)
                    val userName = cursor.getString(col)

                    col = cursor.getColumnIndex(DBHelper.USERS_ID_COL)
                    val clientId = cursor.getString(col)
                    //add payment notify
                    //val dB2 = DBHelper(this)
                    dB.insertNotify(
                        userId.toInt(),
                        "Client",
                        "Fix $userName's Vehicle?",
                        "client",
                        clientId,
                        "DetailsClient"
                    )
                }while(cursor.moveToNext())
            } catch (e: Exception) {
                Toast.makeText(this, e.message + " WelBus", Toast.LENGTH_LONG).show()
            }
        }
    }


}