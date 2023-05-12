package com.jksapps.mechanicconnect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class AdapterMapMarker(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        // 1. Get tag
        val aMarker = marker?.tag as? DataAddressMarkers ?: return null

        // 2. Inflate view and set title, address, and rating
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_map_markers, null
        )
        view.findViewById<TextView>(
            R.id.map_marker_title
        ).text = aMarker.name
        view.findViewById<TextView>(
            R.id.map_marker_address
        ).text = aMarker.address
        view.findViewById<TextView>(
            R.id.map_marker_rating
        ).text = aMarker.id

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }

}