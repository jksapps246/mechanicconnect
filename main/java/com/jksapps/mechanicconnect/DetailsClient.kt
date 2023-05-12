package com.jksapps.mechanicconnect

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi

class DetailsClient : AppCompatActivity() {

    private lateinit var userName : TextView
    private lateinit var cName  : TextView
    private lateinit var cAddress : TextView
    private lateinit var cVehicleType : TextView
    private lateinit var cVehicleName : TextView
    private lateinit var cVehicleModel : TextView
    private lateinit var cVehicleImage : ImageView
    private lateinit var userId : String
    private lateinit var clientId : String

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_client)

        clientId = intent.getStringExtra("clientId").toString()
        userId = intent.getStringExtra("userId").toString()
        userName = findViewById(R.id.clientUserName)
        cName = findViewById(R.id.clientName)
        cAddress = findViewById(R.id.clientAddress)
        cVehicleType = findViewById(R.id.clientVehicleType)
        cVehicleName = findViewById(R.id.clientVehicleName)
        cVehicleModel = findViewById(R.id.clientVehicleModel)
        cVehicleImage = findViewById(R.id.clientVehicleImage)

        if(clientId != null) {
            getUserDetails(clientId)
            getVehicleDetails(clientId)
        }
        else {
            Toast.makeText(this,"No Client found",Toast.LENGTH_SHORT).show()
            finish()
        }

    }
   private fun getUserDetails(clientId: String){
       val dB = DBHelper(this)
       userName.text = dB.getUsername(clientId)
       val contentValues = dB.getAccountDetails(clientId)
       val tempName = contentValues.getAsString(DBHelper.ACCOUNT_FIRST_NAME_COL) + " " + contentValues.getAsString(DBHelper.ACCOUNT_LAST_NAME_COL)
       cName.text = tempName
       val tempAddress = contentValues.getAsString(DBHelper.ACCOUNT_ADDRESS1_COL) + ", " +
               contentValues.getAsString(DBHelper.ACCOUNT_ADDRESS2_COL) + ", " +
               contentValues.getAsString(DBHelper.ACCOUNT_CITY_COL) + ", " +
               contentValues.getAsString(DBHelper.ACCOUNT_POSTAL_CODE_COL)
       cAddress.text = tempAddress
       dB.close()
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getVehicleDetails(clientId: String) {
        val dB = DBHelper(this)
        val contentValues = dB.getUserVehicle(clientId)
        if(!contentValues.isEmpty){
            var data = contentValues.getAsString(DBHelper.VEHICLE_TYPE_COL).toString()
            cVehicleType.text = data
            data = contentValues.getAsString(DBHelper.VEHICLE_NAME_COL).toString()
            cVehicleName.text = data
            data = contentValues.getAsString(DBHelper.VEHICLE_MODEL_COL).toString()
            cVehicleModel.text = data
            val image = contentValues.getAsByteArray(DBHelper.VEHICLE_IMAGE_COL)

            val bmp = BitmapFactory.decodeByteArray(image, 0, image.size)
            cVehicleImage.setImageBitmap(bmp)

        }
    }
    fun cancel(v: View){
        finish()
    }
    fun accept(v: View){
        //update mechanic table accept flag
        val dB = DBHelper(this)
        val check = dB.acceptMechanicUser(clientId,userId)
        if(check){
            //remove notify
            if(dB.deleteNotify(userId,"Client")){
                Toast.makeText(this,"Client Added", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    //back to welcome page
                    val nextPage = Intent(this, WelcomeBusiness::class.java)
                    nextPage.putExtra("userId",userId)
                    nextPage.putExtra("isMechanic",true)
                    startActivity(nextPage)
                }, 3000)
            }
            else {
                Toast.makeText(this,"Warning, (notify delete error), Contact Support",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Warning, Client Couldn't Be Added, Contact Support",Toast.LENGTH_SHORT).show()

        }
        //
    }

}