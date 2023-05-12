package com.jksapps.mechanicconnect

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FragmentHome : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<DataNotify>
    private var imageId : Array<Int> = arrayOf()
    private var heading : Array<String> = arrayOf()
    private var desc : Array<String> = arrayOf()
    private var action : Array<String> = arrayOf()
    private var userData : Array<String> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initialize

        activity?.let {
            //client or business?
            //val isMechanic = activity?.intent?.getBooleanExtra("isMechanic",false)
            //get UserId
            val userId = activity?.intent?.getStringExtra("userId")
            //set up list of items
            //setUpNotify()
            setUpNotify(userId)
            setupToDoListViews(view,userId.toString())
            //setupLatestListViews(view,userId.toString())
        }
    }

    private fun setupToDoListViews(view: View, userId: String) {
        newRecyclerView = view.findViewById(R.id.toDoRView)
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()

        for(i in imageId.indices){
            val note = DataNotify(userId,heading[i],desc[i],imageId[i],action[i],userData[i])
            newArrayList.add(note)
        }
        val nothing = view.findViewById<TextView>(R.id.nothingText1)
        //show and hide "nothing text"
        if(imageId.isNotEmpty()){
            nothing.visibility = View.INVISIBLE
        }
        else {
            nothing.visibility = View.VISIBLE
        }

        val notifyAdapter = AdapterList(newArrayList)
        newRecyclerView.adapter = notifyAdapter

        notifyAdapter.onItemClick = {
            val welcome = activity?.intent?.getStringExtra("welcome")
            val isMechanic = activity?.intent?.getBooleanExtra("isMechanic",false)
            val className = Class.forName("com.jksapps.mechanicconnect.${it.action}")
            val nextPage = Intent(activity, className)
            nextPage.putExtra("userId",userId)
            nextPage.putExtra("welcome",welcome)
            nextPage.putExtra("isMechanic",isMechanic)
            if(it.action == "DetailsClient")
                nextPage.putExtra("clientId",it.userData)
            startActivity(nextPage)
        }
    }
    private fun setupLatestListViews(view: View, userId: String) {
        newRecyclerView = view.findViewById(R.id.latestRView)
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<DataNotify>()

        for(i in imageId.indices){
            val note = DataNotify(userId,heading[i],desc[i],imageId[i],action[i],userData[i])
            newArrayList.add(note)
        }
        val notifyAdapter = AdapterList(newArrayList)
        newRecyclerView.adapter = notifyAdapter

        notifyAdapter.onItemClick = {
            val welcome = activity?.intent?.getStringExtra("welcome")
            val className = Class.forName("com.jksapps.mechanicconnect.${it.action}")
            val nextPage = Intent(activity, className)
            nextPage.putExtra("userId",userId)
            nextPage.putExtra("welcome",welcome)
            startActivity(nextPage)
        }
    }
    private fun setUpNotify(userId: String?){
        val dB = DBHelper(activity)
        val cursor = dB.checkNotify(userId.toString())
        if(cursor != null && cursor.count > 0) {
            try {
                cursor!!.moveToFirst()
                do {
                    //get image from database
                    var col = cursor.getColumnIndex(DBHelper.NOTIFY_IMAGE_COL)
                    var data = cursor.getString(col)
                    val dl = resources.getIdentifier(data, "drawable", activity?.packageName)
                    //add image reference to image list
                    imageId += dl

                    //get heading from database
                    col = cursor.getColumnIndex(DBHelper.NOTIFY_TYPE_COl)
                    data = cursor.getString(col)
                    //add heading to heading list
                    heading += data

                    //get description from database
                    col = cursor.getColumnIndex(DBHelper.NOTIFY_DESCRIPTION_COL)
                    data = cursor.getString(col)
                    //add description to heading list
                    desc += data

                    //get action from database
                    col = cursor.getColumnIndex(DBHelper.NOTIFY_ACTION_COL)
                    data = cursor.getString(col)
                    //add description to heading list
                    action += data

                    //get data/userId from database
                    col = cursor.getColumnIndex(DBHelper.NOTIFY_DATE_COL)
                    data = cursor.getString(col)
                    //add description to heading list
                    userData += data


                } while (cursor.moveToNext())
            } catch (e: Exception) {
                Toast.makeText(context, e.message + " FHome", Toast.LENGTH_SHORT).show()
            }
        }
        dB.close()
    }
}