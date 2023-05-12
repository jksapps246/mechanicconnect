package com.jksapps.mechanicconnect

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout



class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //open database
        val dB = DBHelper(this)
        //check auto login for user
        val cursor = dB.checkAutoLogin()
        if(cursor != null){//check if data is in the table
            if(cursor.count > 0 ) {//Data in database
                //first row
                cursor!!.moveToFirst()
                //get remember flag
                var col = cursor.getColumnIndex(DBHelper.USERS_REMEMBER_COL)
                var remember = cursor.getInt(col)
                if (remember == 1) {//check if remember flag is true
                    //get username
                    col = cursor.getColumnIndex(DBHelper.USERS_USERNAME_COl)
                    val username = cursor.getString(col)
                    col = cursor.getColumnIndex(DBHelper.USERS_ID_COL)
                    val userId = cursor.getString(col)
                    //welcome message
                    val welcome = "Welcome, $username"
                    gotoWelcomePage(userId.toString(),welcome)
                    //show welcome message
                    Toast.makeText(this, welcome, Toast.LENGTH_LONG).show()
                }
            }
            cursor.close()//close database
        }
    }//onCreate()


    //function to login the member
    fun login(v: View){
        //get variable for login screen
        val usernameL = findViewById<EditText>(R.id.usernameLogin)
        val passwordL = findViewById<TextInputLayout>(R.id.passwordLogin)
        val rememberMeL = findViewById<CheckBox>(R.id.rememberMe)
        val dB = DBHelper(this)
        //check for empty fields
        if (usernameL.text.isEmpty()) {
            usernameL.error = "Missing UserName"
        }
        else if (passwordL.editText!!.text.isEmpty()) {
            passwordL.editText!!.error = "Missing Password"
        }
        else {
            //check password
            val userId = dB.checkUsernamePassword(usernameL.text.toString(),passwordL.editText!!.text.toString())
            if(userId > 0){
                //Toast.makeText(this, "Successfully Login", Toast.LENGTH_LONG).show()

                 //update database user to remember
                 if (rememberMeL.isChecked) {
                     val updateR = dB.updateRemember(usernameL.text.toString())
                     if (!updateR) {//cannot update database with remember
                        rememberMeL.isChecked = false
                     }
                 }
                 //set welcome message
                 val welcome = "Welcome, " + usernameL.text.toString()
                 gotoWelcomePage(userId.toString(),welcome)

                //clear fields
                 passwordL.editText!!.text.clear()
                Toast.makeText(this, welcome, Toast.LENGTH_LONG).show()
            }
            else {
             Toast.makeText(this, "Credentials Incorrect, Please Try Again!", Toast.LENGTH_LONG).show()
            }
        }
    }
    //function to go to register page
    fun gotoRegisterPage(v: View){
        val nextPage = Intent(this, Register::class.java)
        startActivity(nextPage)
    }

    private fun gotoWelcomePage(userId: String?, welcome: String?){
        val dB = DBHelper(this)
        val isMechanic = dB.checkUsernameMechanic(userId)
        //check if user is a mechanic or client
        if(!dB.checkAccountDetails(userId)){
            //goto setup page
            val nextPage = Intent(this, Setup::class.java)
            nextPage.putExtra("welcome",welcome)
            nextPage.putExtra("userId",userId)
            nextPage.putExtra("isMechanic",isMechanic)
            startActivity(nextPage)
        }
        else if(isMechanic){
            //goto business page
            val nextPage = Intent(this, WelcomeBusiness::class.java)
            nextPage.putExtra("welcome",welcome)
            nextPage.putExtra("userId",userId.toString())
            nextPage.putExtra("isMechanic",isMechanic)
            startActivity(nextPage)
        }
        else {
            //goto client page
            val nextPage = Intent(this, WelcomeClient::class.java)
            nextPage.putExtra("welcome",welcome)
            nextPage.putExtra("userId",userId.toString())
            nextPage.putExtra("isMechanic",isMechanic)
            startActivity(nextPage)
        }
    }

}