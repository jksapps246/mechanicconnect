package com.jksapps.mechanicconnect

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FragmentGarage : Fragment() {
    private lateinit var userId : String
    private  var mechanicId : Int = 0
    private lateinit var vButton : Button
    private lateinit var mButton : Button
    private lateinit var vText : TextView
    private lateinit var vehicleLayout : ConstraintLayout
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<DataVehicleServices>
    private var sImages : Array<Int> = arrayOf()
    private var sNames : Array<String> = arrayOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garage, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initialize

        activity?.let {
            userId = activity?.intent?.getStringExtra("userId").toString()
            vButton = view.findViewById(R.id.vehicleAddBtn)
            mButton = view.findViewById(R.id.mechanicAddBtn)
            vehicleLayout = view.findViewById(R.id.cv_layout)
            vText = view.findViewById(R.id.noVehicleTxt)
            newRecyclerView = view.findViewById(R.id.servicesRV)
            showVehicleDetails()
            addStandardServices()
            setupServiceViews(view)
            vButton.setOnClickListener{
                goToVehicle()
            }
            mButton.setOnClickListener {
                goToMechanic()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showVehicleDetails(){
        val dB = DBHelper(context)
        val contentValues = dB.getUserVehicle(userId)
        if(!contentValues.isEmpty){
            //change visibility on vehicle layout
            //change Add vehicle button to Change Vehicle
            vText.visibility = View.GONE
            vButton.text = "Change"
            vehicleLayout.visibility = View.VISIBLE

            val vType = view?.findViewById<TextView>(R.id.vTypeTxt)
            val vName = view?.findViewById<TextView>(R.id.vNameTxt)
            val vModel = view?.findViewById<TextView>(R.id.vModelTxt)
            val vImage = view?.findViewById<ImageView>(R.id.vImage)
            var data = contentValues.getAsString(DBHelper.VEHICLE_TYPE_COL).toString()
            vType?.text = data
            data = contentValues.getAsString(DBHelper.VEHICLE_NAME_COL).toString()
            vName?.text = data
            data = contentValues.getAsString(DBHelper.VEHICLE_MODEL_COL).toString()
            vModel?.text = data
            val image = contentValues.getAsByteArray(DBHelper.VEHICLE_IMAGE_COL)

            val bmp = BitmapFactory.decodeByteArray(image, 0, image.size)
            vImage?.setImageBitmap(bmp)

        }
        else{
            vText.visibility = View.VISIBLE
            vButton.text = "Add"
            vehicleLayout.visibility = View.GONE
        }
    }

    private fun goToVehicle() {
        val nextPage = Intent(context, DetailsVehicle::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("isMechanic",false)
        startActivity(nextPage)
    }
    private fun goToMechanic() {
        val nextPage = Intent(context, DetailsMechanic::class.java)
        nextPage.putExtra("userId",userId)
        nextPage.putExtra("mechanicId",mechanicId.toString())
        nextPage.putExtra("isMechanic",false)
        startActivity(nextPage)
    }

    private fun setupServiceViews(view: View) {

        newRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()

        for(i in sImages.indices){
            val note = DataVehicleServices(sImages[i],sNames[i])
            newArrayList.add(note)
        }

        val adapterService = AdapterService(newArrayList)
        newRecyclerView.adapter = adapterService

        adapterService.onItemClick = {
            //add service to schedule
        }
    }
    private fun addStandardServices(){
        val dB = DBHelper(activity)
        val noService = view?.findViewById<TextView>(R.id.noServiceTxt)
        //get mechanic id if the mechanic accepted the client
        mechanicId = dB.getMechanicUserAccepted(userId)
        if(mechanicId > 0){
            try {
                val cursor = dB.getServices(mechanicId.toString(),"Standard")
                if(cursor != null && cursor.count > 0) {
                    //hide "No Service" flag
                    noService?.visibility = View.GONE
                    newRecyclerView.visibility = View.VISIBLE
                    mButton.text = "Change"
                    //goto first row
                    cursor!!.moveToFirst()
                    do {
                        //get image from database
                        var col = cursor.getColumnIndex(DBHelper.SERVICE_IMAGE_COL)
                        var data = cursor.getString(col)
                        val dl = resources.getIdentifier(data, "drawable", activity?.packageName)
                        //add image reference to image list
                        sImages += dl

                        //get heading from database
                        col = cursor.getColumnIndex(DBHelper.SERVICE_NAME_COL)
                        data = cursor.getString(col)
                        //add heading to heading list
                        sNames += data

                    } while (cursor.moveToNext())
                }
                else {
                    //no services, show "No services" flag
                    mButton.text = "Add"
                    noService?.visibility = View.VISIBLE
                    newRecyclerView.visibility = View.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message + " FGarage", Toast.LENGTH_SHORT).show()
            }
        }
        dB.close()
    }
}