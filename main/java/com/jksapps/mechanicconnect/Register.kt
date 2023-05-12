package com.jksapps.mechanicconnect

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Calendar


class Register : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    //function to register a member
    @RequiresApi(Build.VERSION_CODES.O)
    fun register(v: View) {
        //get variables for register page
        val usertype = findViewById<CheckBox>(R.id.isMachanic)
        val usernameReg = findViewById<EditText>(R.id.usernameR)
        val passwordReg1 = findViewById<EditText>(R.id.passwordR)
        val passwordReg2 = findViewById<EditText>(R.id.passwordR2)
        val dB = DBHelper(this)

        var isMac = 0;
        //check for mechanic account
        if(usertype.isChecked) isMac = 1

        //Prompt User for missing fields
        if (usernameReg.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing User Name", Toast.LENGTH_LONG).show()
        }
        else if (passwordReg1.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Password", Toast.LENGTH_LONG).show()
        }
        else if (passwordReg2.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Re-password", Toast.LENGTH_LONG).show()
        }
        else {//all fields are populated
            if(passwordReg1.text.toString() == passwordReg2.text.toString()) {//both passwords are equal

                var chkUser = dB.checkUsername(usernameReg.text.toString())
                if(!chkUser){//check if username doesn't exist
                    var insertD = dB.insertUserData(usernameReg.text.toString(),passwordReg1.text.toString(),0, isMac)
                    if(insertD){//if insert is successful
                        //welcome message
                        val welcome = "Welcome, " + usernameReg.text.toString()
                        //get new generated userId
                        val userId = dB.checkUsernamePassword(usernameReg.text.toString(),passwordReg1.text.toString())
                        if(userId > 0) {
                            Toast.makeText(this, "Registered successfully!", Toast.LENGTH_LONG)
                                .show()
                            //add notify(s)
                            addNotify(userId,isMac)
                            addEmailValidation(userId)
                            //add mechanic standard services
                            if(isMac == 1){
                                addMechanicServices(userId)
                            }
                            //goto setup page
                            val macCheck = isMac > 0
                            val nextPage = Intent(this, Setup::class.java)
                            nextPage.putExtra("welcome",welcome)
                            nextPage.putExtra("userId",userId.toString())
                            nextPage.putExtra("isMechanic",macCheck)
                            startActivity(nextPage)

                        }//get UserId fail
                        else Toast.makeText(this, "Warning! User not found, Please Contact Support", Toast.LENGTH_LONG).show()
                    }//insert fail
                    else Toast.makeText(this, "Warning! Unable to register User, Please Contact Support", Toast.LENGTH_LONG).show()
                }//username already exists
                else Toast.makeText(this, "Warning! User Name Already Exists", Toast.LENGTH_LONG).show()
            }//passwords not equal
            else Toast.makeText(this, "Warning! Passwords Are not Equal", Toast.LENGTH_LONG).show()
        }

    }//register()

    fun gotoLoginPage(v: View){
        val nextPage = Intent(this, Login::class.java)
        startActivity(nextPage)
    }//gotoLoginPage()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNotify(userId: Int, mechanic: Int){
        try {
            val dB = DBHelper(this)
            val date = LocalDateTime.now().toString()
            //add payment notify
            dB.insertNotify(
                userId,
                "Payment",
                "Add Payment details.",
                "pay",
                date,
                "Setup"
            )
            //add email notify
            dB.insertNotify(
                userId,
                "Email",
                "Please validate Email.",
                "email",
                date,
                "EmailValidation"
            )
            if(mechanic == 0) {
                //add mechanic notify
                dB.insertNotify(
                    userId,
                    "Mechanic",
                    "Please add a mechanic.",
                    "mechanic",
                    date,
                    "Mechanic"
                )
            }
        }
        catch(e: Exception){
            Toast.makeText(this,e.message +" Reg1",Toast.LENGTH_LONG).show()
        }

    }
    private fun addEmailValidation(userId: Int){
        try {
            val dB = DBHelper(this)
            //add email validation
            dB.insertEmailValidation(
                userId,
                1234
            )
        }
        catch(e: Exception){
            Toast.makeText(this,e.message + "Reg2",Toast.LENGTH_LONG).show()
        }

    }
    private fun addMechanicServices(userId: Int){
        val dB = DBHelper(this)
        try{
            //add new services
            var sImage = resources.getIdentifier("sparkplug","drawable", packageName)
            dB.insertService(userId,"Spark Plug",sImage,"Standard")

            sImage = resources.getIdentifier("brakes","drawable", packageName)
            dB.insertService(userId,"Brakes",sImage,"Standard")

            sImage = resources.getIdentifier("battery","drawable", packageName)
            dB.insertService(userId,"Battery",sImage,"Standard")

            sImage = resources.getIdentifier("oil","drawable", packageName)
            dB.insertService(userId,"Oil Change",sImage,"Standard")

            sImage = resources.getIdentifier("engine","drawable", packageName)
            dB.insertService(userId,"Check Engine",sImage,"Standard")

            sImage = resources.getIdentifier("alignment","drawable", packageName)
            dB.insertService(userId,"Wheel Alignment",sImage,"Standard")

            sImage = resources.getIdentifier("diagnostics","drawable", packageName)
            dB.insertService(userId,"Diagnostics",sImage,"Standard")

        }catch(e: Exception){
            Toast.makeText(this,e.message + " Reg3", Toast.LENGTH_SHORT).show()
        }
    }
}




