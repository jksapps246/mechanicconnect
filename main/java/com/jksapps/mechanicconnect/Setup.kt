package com.jksapps.mechanicconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView


class Setup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        //custom image in action bar
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_bar_logo, null)
        actionBar.customView = view

        val myDb = DBHelper(this)
        val userId = intent.getStringExtra("userId")
        //check that no more setup is needed
        //account details
        if(myDb.checkAccountDetails(userId)){
            //populate account fields
            populateAccountDetails(userId.toString())
            //disable account details
            disableAccountDetails()
            //enable continue button
            val cont = findViewById<Button>(R.id.continueBtn)
            cont.visibility = View.VISIBLE
        }
        //payment details
        if(myDb.checkPaymentDetails(userId)){
            //populate payment fields
            populatePaymentDetails(userId.toString())
            //disable Payment details
            disablePaymentDetails()
        }
    }
    fun saveAccountDetails(v: View){
        val fName = findViewById<EditText>(R.id.editFirstName)
        val lName = findViewById<EditText>(R.id.editLastName)
        val email = findViewById<EditText>(R.id.editEmail)
        val address1 = findViewById<EditText>(R.id.editAddress1)
        val address2 = findViewById<EditText>(R.id.editAddress2)
        val city = findViewById<EditText>(R.id.editCity)
        val postalCode = findViewById<EditText>(R.id.editPostalCode)
        val phone = findViewById<EditText>(R.id.editPhone)
        val editBtn = findViewById<TextView>(R.id.editADTxt)
        val saveBtn = findViewById<Button>(R.id.saveBtnA)
        val cont = findViewById<Button>(R.id.continueBtn)

        if (fName.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing First Name", Toast.LENGTH_LONG).show()
        }
        else if (lName.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Last Name", Toast.LENGTH_LONG).show()
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            Toast.makeText(this, "Warning! Invalid Email Address", Toast.LENGTH_LONG).show()
        }
        else if (address1.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Street Address", Toast.LENGTH_LONG).show()
        }
        else if (city.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing City", Toast.LENGTH_LONG).show()
        }
        else if (postalCode.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Postal Code", Toast.LENGTH_LONG).show()
        }
        else if (phone.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Phone Number", Toast.LENGTH_LONG).show()
        }
        else {
            val myDb = DBHelper(this)
            val userId =  intent.getStringExtra("userId")
            if(myDb.insertAccountData(
                    Integer.parseInt(userId),
                    fName.text.toString(),
                    lName.text.toString(),
                    email.text.toString(),
                    address1.text.toString(),
                    address2.text.toString(),
                    city.text.toString(),
                    postalCode.text.toString(),
                    phone.text.toString())){//data Added
                Toast.makeText(this,"Account Details Updated!",Toast.LENGTH_SHORT).show()

                //disable details
                disableAccountDetails()
            }
            else {//some issue
                Toast.makeText(this,"Warning, Data not added, Contact Support.",Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun disableAccountDetails(){
        val fName = findViewById<EditText>(R.id.editFirstName)
        val lName = findViewById<EditText>(R.id.editLastName)
        val email = findViewById<EditText>(R.id.editEmail)
        val address1 = findViewById<EditText>(R.id.editAddress1)
        val address2 = findViewById<EditText>(R.id.editAddress2)
        val city = findViewById<EditText>(R.id.editCity)
        val postalCode = findViewById<EditText>(R.id.editPostalCode)
        val phone = findViewById<EditText>(R.id.editPhone)
        val editBtn = findViewById<TextView>(R.id.editADTxt)
        val saveBtn = findViewById<Button>(R.id.saveBtnA)
        //disable edit text
        fName.isEnabled = false
        lName.isEnabled = false
        email.isEnabled = false
        address1.isEnabled = false
        address2.isEnabled = false
        city.isEnabled = false
        postalCode.isEnabled = false
        phone.isEnabled = false

        //show edit button
        editBtn.visibility = View.VISIBLE

        //hide save button
        saveBtn.visibility = View.INVISIBLE
    }
    private fun disablePaymentDetails(){
        val fullName = findViewById<EditText>(R.id.editFullName)
        val address1 = findViewById<EditText>(R.id.editBAddress1)
        val address2 = findViewById<EditText>(R.id.editBAddress2)
        val city = findViewById<EditText>(R.id.editBCity)
        val postalCode = findViewById<EditText>(R.id.editBPostalCode)
        val cardNumber = findViewById<EditText>(R.id.editCardNumber)
        val eDate = findViewById<EditText>(R.id.editExpirationDate)
        val code = findViewById<EditText>(R.id.editCode)
        val editBtn = findViewById<TextView>(R.id.editPDTxt)
        val saveBtn = findViewById<Button>(R.id.saveBtnP)
        //disable edit text
        fullName.isEnabled = false
        address1.isEnabled = false
        address2.isEnabled = false
        city.isEnabled = false
        postalCode.isEnabled = false
        cardNumber.isEnabled = false
        eDate.isEnabled = false
        code.isEnabled = false

        //show edit button
        editBtn.visibility = View.VISIBLE

        //hide save button
        saveBtn.visibility = View.INVISIBLE
    }
    fun editAccountDetails(v: View){
        val fName = findViewById<EditText>(R.id.editFirstName)
        val lName = findViewById<EditText>(R.id.editLastName)
        val email = findViewById<EditText>(R.id.editEmail)
        val address1 = findViewById<EditText>(R.id.editAddress1)
        val address2 = findViewById<EditText>(R.id.editAddress2)
        val city = findViewById<EditText>(R.id.editCity)
        val postalCode = findViewById<EditText>(R.id.editPostalCode)
        val phone = findViewById<EditText>(R.id.editPhone)
        val editBtn = findViewById<TextView>(R.id.editADTxt)
        val saveBtn = findViewById<Button>(R.id.saveBtnA)
        //enable edit text
        fName.isEnabled = true
        lName.isEnabled = true
        email.isEnabled = true
        address1.isEnabled = true
        address2.isEnabled = true
        city.isEnabled = true
        postalCode.isEnabled = true
        phone.isEnabled = true

        //hide edit button
        editBtn.visibility = View.INVISIBLE

        //show save button
        saveBtn.visibility = View.VISIBLE
    }
    fun editPaymentDetails(v: View){
        val fullName = findViewById<EditText>(R.id.editFullName)
        val address1 = findViewById<EditText>(R.id.editBAddress1)
        val address2 = findViewById<EditText>(R.id.editBAddress2)
        val city = findViewById<EditText>(R.id.editBCity)
        val postalCode = findViewById<EditText>(R.id.editBPostalCode)
        val cardNumber = findViewById<EditText>(R.id.editCardNumber)
        val eDate = findViewById<EditText>(R.id.editExpirationDate)
        val code = findViewById<EditText>(R.id.editCode)
        val editBtn = findViewById<TextView>(R.id.editPDTxt)
        val saveBtn = findViewById<Button>(R.id.saveBtnP)
        //enable edit text
        fullName.isEnabled = true
        address1.isEnabled = true
        address2.isEnabled = true
        city.isEnabled = true
        postalCode.isEnabled = true
        cardNumber.isEnabled = true
        eDate.isEnabled = true
        code.isEnabled = true

        //hide edit button
        editBtn.visibility = View.INVISIBLE

        //show save button
        saveBtn.visibility = View.VISIBLE
    }
    fun savePaymentDetails(v: View){
        val fullName = findViewById<EditText>(R.id.editFullName)
        val address1 = findViewById<EditText>(R.id.editBAddress1)
        val address2 = findViewById<EditText>(R.id.editBAddress2)
        val city = findViewById<EditText>(R.id.editBCity)
        val postalCode = findViewById<EditText>(R.id.editBPostalCode)
        val cardNumber = findViewById<EditText>(R.id.editCardNumber)
        val eDate = findViewById<EditText>(R.id.editExpirationDate)
        val code = findViewById<EditText>(R.id.editCode)
        val editBtn = findViewById<TextView>(R.id.editPDTxt)
        val saveBtn = findViewById<Button>(R.id.saveBtnP)

        if (fullName.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Full Name", Toast.LENGTH_LONG).show()
        }
        else if (address1.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Street Address", Toast.LENGTH_LONG).show()
        }
        else if (city.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing City", Toast.LENGTH_LONG).show()
        }
        else if (postalCode.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Postal Code", Toast.LENGTH_LONG).show()
        }
        else if (cardNumber.text.isEmpty() || cardNumber.textSize < 16) {
            Toast.makeText(this, "Warning! Enter Card Number", Toast.LENGTH_LONG).show()
        }
        else if (eDate.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Expiration Date", Toast.LENGTH_LONG).show()
        }
        else if (code.text.isEmpty()) {
            Toast.makeText(this, "Warning! Missing Card Code", Toast.LENGTH_LONG).show()
        }
        else {
            val myDb = DBHelper(this)
            val userId =  intent.getStringExtra("userId")
            if(myDb.insertPaymentData(
                    Integer.parseInt(userId),
                    fullName.text.toString(),
                    address1.text.toString(),
                    address2.text.toString(),
                    city.text.toString(),
                    postalCode.text.toString(),
                    cardNumber.text.toString(),
                    eDate.text.toString(),
                    code.text.toString()
                    )){//data Added

                //disable payment details
                disablePaymentDetails()
                //take away payment notify
                myDb.deleteNotify(userId,"Payment")

                Toast.makeText(this,"Payment Details Updated!",Toast.LENGTH_SHORT).show()
            }
            else {//some issue
                Toast.makeText(this,"Warning, Data not added, Contact Support.",Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun copyAddress(v: View){
        val address1 = findViewById<EditText>(R.id.editAddress1)
        val address2 = findViewById<EditText>(R.id.editAddress2)
        val city = findViewById<EditText>(R.id.editCity)
        val postalCode = findViewById<EditText>(R.id.editPostalCode)

        val bAddress1 = findViewById<EditText>(R.id.editBAddress1)
        val bAddress2 = findViewById<EditText>(R.id.editBAddress2)
        val bCity = findViewById<EditText>(R.id.editBCity)
        val bPostalCode = findViewById<EditText>(R.id.editBPostalCode)

        if (address1.text.isNotEmpty()) {
            bAddress1.text = address1.text
        }
        if (address2.text.isNotEmpty()) {
            bAddress2.text = address2.text
        }
        if (city.text.isNotEmpty()) {
            bCity.text = city.text
        }
        if (postalCode.text.isNotEmpty()) {
            bPostalCode.text = postalCode.text
        }
        Toast.makeText(this,"Address Copied.",Toast.LENGTH_SHORT).show()
    }
    fun continueOn(v: View){
        val myDb = DBHelper(this)
        val userId = intent.getStringExtra("userId")
        //check that no more setup is needed
        //account details
        if(myDb.checkAccountDetails(userId)) {
            //goto Welcome Page
            gotoWelcomePage()
        }
        else {
            Toast.makeText(this, "Please add account details before continuing",Toast.LENGTH_SHORT).show()
        }
    }
    private fun gotoWelcomePage(){
        val dB = DBHelper(this)
        val welcome = intent.getStringExtra("welcome")
        val userId = intent.getStringExtra("userId")
        val isMechanic = intent.getBooleanExtra("isMechanic",false)

        if(isMechanic){
            val nextPage = Intent(this, WelcomeBusiness::class.java)
            //send welcome message to welcome business page
            nextPage.putExtra("welcome",welcome)
            nextPage.putExtra("userId",userId)
            nextPage.putExtra("isMechanic",isMechanic)
            startActivity(nextPage)
        }
        else {//goto welcome client page
            val nextPage = Intent(this, WelcomeClient::class.java)
            //send welcome message to welcome business page
            nextPage.putExtra("welcome",welcome)
            nextPage.putExtra("userId",userId)
            nextPage.putExtra("isMechanic",isMechanic)
            startActivity(nextPage)
        }
        //show welcome message
        Toast.makeText(this, welcome, Toast.LENGTH_SHORT).show()
        dB.close()
    }
    private fun populateAccountDetails(userId: String) {
        val myDB = DBHelper(this)
        val aDetails = myDB.getAccountDetails(userId)
        val fName = findViewById<EditText>(R.id.editFirstName)
        val lName = findViewById<EditText>(R.id.editLastName)
        val email = findViewById<EditText>(R.id.editEmail)
        val address1 = findViewById<EditText>(R.id.editAddress1)
        val address2 = findViewById<EditText>(R.id.editAddress2)
        val city = findViewById<EditText>(R.id.editCity)
        val postalCode = findViewById<EditText>(R.id.editPostalCode)
        val phone = findViewById<EditText>(R.id.editPhone)
        if(aDetails != null){
            //add account details to each edit text
            fName.setText(aDetails.getAsString(DBHelper.ACCOUNT_FIRST_NAME_COL))
            lName.setText(aDetails.getAsString(DBHelper.ACCOUNT_LAST_NAME_COL))
            email.setText(aDetails.getAsString(DBHelper.ACCOUNT_EMAIL_COL))
            address1.setText(aDetails.getAsString(DBHelper.ACCOUNT_ADDRESS1_COL))
            address2.setText(aDetails.getAsString(DBHelper.ACCOUNT_ADDRESS2_COL))
            city.setText(aDetails.getAsString(DBHelper.ACCOUNT_CITY_COL))
            postalCode.setText(aDetails.getAsString(DBHelper.ACCOUNT_POSTAL_CODE_COL))
            phone.setText(aDetails.getAsString(DBHelper.ACCOUNT_PHONE_NUMBER_COL))
        }
    }
    private fun populatePaymentDetails(userId: String) {
        val myDB = DBHelper(this)
        val pDetails = myDB.getPaymentDetails(userId)
        val fullName = findViewById<EditText>(R.id.editFullName)
        val address1 = findViewById<EditText>(R.id.editBAddress1)
        val address2 = findViewById<EditText>(R.id.editBAddress2)
        val city = findViewById<EditText>(R.id.editBCity)
        val postalCode = findViewById<EditText>(R.id.editBPostalCode)
        val cardNumber = findViewById<EditText>(R.id.editCardNumber)
        val eDate = findViewById<EditText>(R.id.editExpirationDate)
        val code = findViewById<EditText>(R.id.editCode)
        if(pDetails != null){
            //add payment details to each edit text
            fullName.setText(pDetails.getAsString(DBHelper.PAYMENT_FULL_NAME_BILLING_COL))
            address1.setText(pDetails.getAsString(DBHelper.PAYMENT_ADDRESS_BILLING1_COL))
            address2.setText(pDetails.getAsString(DBHelper.PAYMENT_ADDRESS_BILLING2_COL))
            postalCode.setText(pDetails.getAsString(DBHelper.PAYMENT_POSTAL_CODE_BILLING_COL))
            city.setText(pDetails.getAsString(DBHelper.PAYMENT_CITY_COL))
            cardNumber.setText(pDetails.getAsString(DBHelper.PAYMENT_CARD_NUMBER_COL))
            eDate.setText(pDetails.getAsString(DBHelper.PAYMENT_CARD_NUMBER_DATE_COL))
            code.setText(pDetails.getAsString(DBHelper.PAYMENT_CARD_NUMBER_CODE_COL))


        }
    }
}


