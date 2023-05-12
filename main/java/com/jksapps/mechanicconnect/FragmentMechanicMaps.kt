package com.jksapps.mechanicconnect


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class FragmentMechanicMaps : Fragment() {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2
    private lateinit var id : Array<String>
    private lateinit var name : Array<String>
    private lateinit var businessName : Array<String>
    private lateinit var address : Array<String>
    private var googleMap: GoogleMap? = null
    private val callback = OnMapReadyCallback { myGoogleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        googleMap = myGoogleMap
        setLocation()
        id  = (activity as Mechanic).ids
        name = (activity as Mechanic).names
        businessName = (activity as Mechanic).businessNames
        address = (activity as Mechanic).addresses

        addMechanicMarkers()
        //get location
        //getLocation()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mechanic_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mechanic_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setLocation() {

        val myAddress = activity?.findViewById<TextView>(R.id.editAddressMechanic)
        myAddress?.setText(addMyMarker(myAddress.text.toString()))

    }
    fun addMyMarker(myAddress: String) : String{
        var newAddress = myAddress
        var location: LatLng
        val geocoder = Geocoder(context)
        var addressList = geocoder.getFromLocationName(newAddress, 1)
        try {
            if(addressList != null){
                location = LatLng(addressList[0].latitude, addressList[0].longitude)
                googleMap?.addMarker(MarkerOptions().position(location).title((activity as? Mechanic)!!.name))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11.0f))
                newAddress = addressList[0].getAddressLine(0).toString()
            }
        } catch (e: Exception ) {
            Toast.makeText(context,e.message + " FMechMaps",Toast.LENGTH_SHORT).show()
            addressList = geocoder.getFromLocation(-34.0,151.0,1)
            location = LatLng(addressList[0].latitude, addressList[0].longitude)
            googleMap?.addMarker(MarkerOptions().position(location).title("Marker in Toronto"))
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(location))
            newAddress = addressList[0].getAddressLine(0).toString()
        }
        return newAddress
    }
    private fun addMechanicMarkers() {
        addMarkers()
    }
    private fun addMarkers(){
        val geocoder = Geocoder(context)
        val myAddress = activity?.findViewById<TextView>(R.id.editAddressMechanic)
        val myAddressList = geocoder.getFromLocationName(myAddress?.text.toString(), 1)
       //val myLocation = LatLng(myAddressList[0].latitude, myAddressList[0].longitude)

        for(i in id.indices){
            //markers must be in 100km radius
            val addressList = geocoder.getFromLocationName(address[i], 1)
            val location = LatLng(addressList[0].latitude, addressList[0].longitude)
            val distance = distanceInKm(myAddressList[0].latitude, myAddressList[0].longitude,addressList[0].latitude, addressList[0].longitude)
            if(distance < 100.00) {
                val mIcon = context?.let { BitmapFromVector(it, R.drawable.mechanic_icon) }
                googleMap?.addMarker(MarkerOptions().position(location).title(name[i]).icon(mIcon))
            }
        }
    }
    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)

        // below line is use to create a bitmap for our drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
/*
    private fun getLocation() {
        locationManager = getSystemService( Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        tvGpsLocation = findViewById<TextView>(R.id.textView)
        tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    */
}