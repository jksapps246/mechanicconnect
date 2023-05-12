package com.jksapps.mechanicconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

class DetailsMechanic : AppCompatActivity() {

    private lateinit var mBusinessName : TextView
    private lateinit var mName : TextView
    private lateinit var mAddress : TextView
    private lateinit var mechanicId: String
    private lateinit var standardGV : GridView
    private var sImages : Array<Int> = arrayOf()
    private var sNames : Array<String> = arrayOf()
    private lateinit var newArrayList: ArrayList<DataVehicleServices>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_mechanic)
        mBusinessName = findViewById(R.id.mechanicBusinessName)
        mName = findViewById(R.id.mechanicName)
        mAddress = findViewById(R.id.mechanicAddress)
        mechanicId = intent.getStringExtra("mechanicId").toString()
        standardGV = findViewById(R.id.standardGV)
        getMechanicDetails()
        addStandardServices()
        setupServiceViews()
    }
    private fun getMechanicDetails(){
        val dB = DBHelper(this)
        try{
            val cursor = dB.getMechanicDetails(mechanicId)
            if(cursor != null && cursor.count > 0) {
                //goto first row
                cursor!!.moveToFirst()
                var col = cursor.getColumnIndex("business_name")
                mBusinessName.text = cursor.getString(col)

                col = cursor.getColumnIndex("name")
                mName.text = cursor.getString(col)

                col = cursor.getColumnIndex("address")
                mAddress.text = cursor.getString(col)
            }
        }
        catch (e: Exception){
            Toast.makeText(this,e.message + " DMech1",Toast.LENGTH_SHORT).show()
        }

    }
    private fun setupServiceViews() {

         newArrayList = arrayListOf()

        for(i in sImages.indices){
            val note = DataVehicleServices(sImages[i],sNames[i])
            newArrayList.add(note)
        }

        val adapterService = AdapterServiceGrid(newArrayList,this)
        standardGV.adapter = adapterService

        standardGV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying
            // a toast message with course name.
            Toast.makeText(
                applicationContext, newArrayList[position].sName + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun addStandardServices(){
        val dB = DBHelper(this)
        try {
            val cursor = dB.getServices(mechanicId.toString(),"Standard")
            if(cursor != null && cursor.count > 0) {
                //goto first row
                cursor!!.moveToFirst()
                do {
                    //get image from database
                    var col = cursor.getColumnIndex(DBHelper.SERVICE_IMAGE_COL)
                    var data = cursor.getString(col)
                    val dl = resources.getIdentifier(data, "drawable", packageName)
                    //add image reference to image list
                    sImages += dl

                    //get heading from database
                    col = cursor.getColumnIndex(DBHelper.SERVICE_NAME_COL)
                    data = cursor.getString(col)
                    //add heading to heading list
                    sNames += data

                } while (cursor.moveToNext())
            }

        } catch (e: Exception) {
            Toast.makeText(this, e.message + " DMech2", Toast.LENGTH_SHORT).show()
        }
        dB.close()

    }
}
