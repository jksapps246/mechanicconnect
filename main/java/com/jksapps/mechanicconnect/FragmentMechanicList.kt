package com.jksapps.mechanicconnect

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentMechanicList : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<DataAddressMarkers>
    private lateinit var id : Array<String>
    private lateinit var name : Array<String>
    private lateinit var businessName : Array<String>
    private lateinit var address : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mechanic_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initialize
        activity?.let {
            //get UserId
            val userId = activity?.intent?.getStringExtra("userId")
            //set up list of items
            //setUpNotify()
            id  = (activity as Mechanic).ids
            name = (activity as Mechanic).names
            businessName = (activity as Mechanic).businessNames
            address = (activity as Mechanic).addresses
            setupAddressList(view,userId.toString())
        }
    }

    private fun setupAddressList(view: View, userId: String) {
        newRecyclerView = view.findViewById(R.id.mRecycerView)
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<DataAddressMarkers>()

        for(i in id.indices){
            val aMech = DataAddressMarkers(id[i],name[i],businessName[i],address[i])
            newArrayList.add(aMech)
        }
        val mechanicList = AdapterAddressList(newArrayList)
        newRecyclerView.adapter = mechanicList

        mechanicList.onItemClick = {
            AlertDialog.Builder(context)
                .setTitle("Mechanic Alert")
                .setMessage("Add ${it.businessName}?")
                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->

                    try{
                        //1)add mechanic to database
                        val dB = DBHelper(context)
                        if(dB.insertMechanicUser(userId.toInt(),it.id.toInt())){
                            Toast.makeText(context,"Mechanic Added!!",Toast.LENGTH_SHORT).show()
                            //2) delete mechanic flag
                            if(dB.deleteNotify(userId,"Mechanic")){
                                //3) goto welcome page
                                val nextPage = Intent(context, WelcomeClient::class.java)
                                nextPage.putExtra("userId",userId)
                                nextPage.putExtra("mechanicId",it.id)
                                startActivity(nextPage)
                            }
                            else {
                                Toast.makeText(context,"Cannot Add Mechanic Notify!",Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            Toast.makeText(context,"Cannot Add Mechanic!",Toast.LENGTH_SHORT).show()
                        }

                    }catch(e: Exception){
                        Toast.makeText(context,e.message + " FMEchList",Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->

                }
                .show()
        }

    }


}