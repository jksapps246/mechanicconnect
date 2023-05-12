package com.jksapps.mechanicconnect

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

 class TabPageAdapterClient(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0 -> FragmentHome()
            1 -> FragmentGarage()
            2 -> FragmentChat()
            else -> FragmentHome()
        }
    }
}