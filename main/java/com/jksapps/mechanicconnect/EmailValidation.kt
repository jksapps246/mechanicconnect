package com.jksapps.mechanicconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class EmailValidation : AppCompatActivity() {

    lateinit var welcome : String
    lateinit var userId : String

     var isMechanic = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_validation)

        val dB = DBHelper(this)
        userId = intent.getStringExtra("userId").toString()

        //get user email and add to text
        val email = findViewById<TextView>(R.id.emailTxt)
        val cDetails = dB.getContactDetails(userId)
        email.text = cDetails.getAsString(DBHelper.ACCOUNT_EMAIL_COL)

    }
    fun validate(v: View){
        val dB = DBHelper(this)
        val code = findViewById<EditText>(R.id.editCode)
        val vBtn = findViewById<Button>(R.id.validateBtn)
        val cBtn = findViewById<Button>(R.id.cancelBtn)

        if(code.text.isEmpty()){
            code.error = "Enter Code"
        }
        else {
            val emailCode = dB.getEmailValidation(userId).toString()
            if(code.text.toString() == emailCode) {//compare codes
                Toast.makeText(this,"Email Validated!",Toast.LENGTH_LONG).show()
                //delete codes
                if(!dB.deleteEmailValidation(userId)){
                    Toast.makeText(this,"Warning, Email Validation, Contact Support!",Toast.LENGTH_LONG).show()
                }
                //remove email notify
                if(!dB.deleteNotify(userId,"Email")){
                    Toast.makeText(this,"Warning, EmailNotify, Contact Support!",Toast.LENGTH_LONG).show()
                }
                //disable buttons
                vBtn.isEnabled = false
                cBtn.isEnabled = false
                //go back after 3 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    gotoWelcomePage()
                }, 3000)

            }
            else {
                code.error = "Invalid Code"
            }

        }
        dB.close()
    }
    fun cancel(v: View){
        finish()
    }
    private fun gotoWelcomePage(){
        //val dB = DBHelper(this)
        welcome = intent.getStringExtra("welcome").toString()
        userId = intent.getStringExtra("userId").toString()
        isMechanic = intent.getBooleanExtra("isMechanic", false)

        //if(dB.checkUsernameMechanic(userId)){
        if(isMechanic){
                val nextPage = Intent(this, WelcomeBusiness::class.java)
                //send welcome message to welcome business page
                nextPage.putExtra("welcome",welcome)
                nextPage.putExtra("userId",userId)
                nextPage.putExtra("isMechanic",true)
                startActivity(nextPage)
            }
        else {//goto welcome client page
            val nextPage = Intent(this, WelcomeClient::class.java)
            //send welcome message to welcome business page
            nextPage.putExtra("welcome",welcome)
            nextPage.putExtra("userId",userId)
            nextPage.putExtra("isMechanic",false)
            startActivity(nextPage)
        }

        //show welcome message
        //Toast.makeText(this, welcome, Toast.LENGTH_SHORT).show()
        //dB.close()
    }
}