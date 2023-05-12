package com.jksapps.mechanicconnect


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout



class WelcomeClient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_client)

        //custom image in action bar
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_bar_logo, null)
        actionBar.customView = view

        val welcome = findViewById<TextView>(R.id.welcomeTxt)
        val message =  intent.getStringExtra("welcome")
        welcome.text = message.toString()
        setUpTabBar()

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
                android.app.AlertDialog.Builder(this@WelcomeClient)
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
        val adapter = TabPageAdapterClient(this, tabLayout.tabCount)
        val viewpager = findViewById<ViewPager2>(R.id.viewpagerC)
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
    }//onBackPressed()
    private fun gotoSetUp(){
        val welcome =  intent.getStringExtra("welcome")
        val userId =  intent.getStringExtra("userId")
        val isMechanic =  intent.getBooleanExtra("isMechanic",false)
        val nextPage = Intent(this, Setup::class.java)
        nextPage.putExtra("welcome",welcome)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }//gotoSetUp()
    private fun gotoUserSettings(){
        val userId =  intent.getStringExtra("userId")
        val isMechanic =  intent.getBooleanExtra("isMechanic",false)
        val nextPage = Intent(this, UserSettings::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }//gotoUserSettings()
    private fun gotoContact(){
        val userId =  intent.getStringExtra("userId")
        val isMechanic =  intent.getBooleanExtra("isMechanic",false)
        val nextPage = Intent(this, Contact::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",isMechanic)
        startActivity(nextPage)
    }//gotoContact()

}