package com.jksapps.mechanicconnect

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream


class DetailsVehicle : AppCompatActivity() {

    private lateinit var userId : String
    private lateinit var vImage : ImageView
    private lateinit var cancelBtn: Button
    private lateinit var addBtn: Button
    private lateinit var loadBtn: Button
    private lateinit var spinnerVT : Spinner
    private lateinit var spinnerVN : Spinner
    private lateinit var spinnerVM : Spinner
    private lateinit var vType : Array<String>
    private lateinit var vName : Array<String>
    private lateinit var vModel : Array<String>

    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle)

        vType = resources.getStringArray(R.array.VehicleType)
        vName = resources.getStringArray(R.array.VehicleName)
        vModel = resources.getStringArray(R.array.VehicleModel)

        spinnerVT = findViewById(R.id.spinnerVT)
        spinnerVN = findViewById(R.id.spinnerVN)
        spinnerVM = findViewById(R.id.spinnerVM)

        vImage = findViewById(R.id.loadVImage)
        cancelBtn = findViewById(R.id.vCancelBtn)
        addBtn = findViewById(R.id.vAddBtn)
        loadBtn = findViewById(R.id.loadImageBtn)
        userId = intent.getStringExtra("userId").toString()

        setVehicleList(vType,spinnerVT)
        setVehicleList(vName,spinnerVN)
        setVehicleList(vModel,spinnerVM)

        cancelBtn.setOnClickListener {
            finish()
        }
        addBtn.setOnClickListener {
            saveVehicle()
        }
        loadBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == RESULT_OK && requestCode == pickImage) {
                imageUri = data?.data
                vImage.setImageURI(imageUri)
            }
        }

    private fun setVehicleList(vehicle: Array<String>, spinner: Spinner) {
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, vehicle
            )
            spinner.adapter = adapter
            /*spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }*/
        }

    }

    private fun saveVehicle(){

        val vehicleType = spinnerVT.selectedItem.toString()
        val vehicleName = spinnerVN.selectedItem.toString()
        val vehicleModel = spinnerVM.selectedItem.toString()


        val bitmap = (vImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val vehicleImage = baos.toByteArray()
        try {
            //check if vehicle already exists and delete it
            val dB = DBHelper(this)
            if (dB.insertVehicle(
                    userId,
                    vehicleType,
                    vehicleName,
                    vehicleModel,
                    vehicleImage
                )
            ) {
                Toast.makeText(this, "Vehicle Details Added", Toast.LENGTH_SHORT).show()
                gotoGarage()
            } else {
                Toast.makeText(this, "Vehicle Details Not Added", Toast.LENGTH_SHORT).show()
            }

        }catch(e: Exception){
            Toast.makeText(this,e.message + " Vehicle",Toast.LENGTH_SHORT).show()
        }
    }
    private fun gotoGarage() {
        val nextPage = Intent(this,WelcomeClient::class.java)
        nextPage.putExtra("welcome",userId)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",true)
        startActivity(nextPage)
    }
}