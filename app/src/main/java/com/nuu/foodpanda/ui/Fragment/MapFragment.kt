package com.nuu.foodpanda.ui.Fragment

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nuu.foodpanda.R
import com.nuu.foodpanda.databinding.FragmentMapBinding
import java.io.IOException
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var address: String
    private lateinit var shopImage: String
    private lateinit var shopName: String

    companion object{
        private var mapFragment: MapFragment? = null
        fun newInstance(): MapFragment? {
            if(mapFragment == null){
                mapFragment = MapFragment()
            }
            return mapFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

//        val textView: TextView = root.findViewById(R.id.text_gallery)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        initData()

        return binding.root
    }

    private fun initData(){
        arguments?.getString("address")?.run {
            address = arguments?.getString("address")!!
            shopImage = arguments?.getString("shopImage")!!
            shopName = arguments?.getString("shopName")!!
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        var addressLocation: List<Address>? = null
        try {
            addressLocation = geoCoder.getFromLocationName(address.trim { it <= ' ' }, 1)
            val latitude = addressLocation[0].latitude
            val longitude = addressLocation[0].longitude
            Log.e("latitude", latitude.toString() + "")
            Log.e("longitude", longitude.toString() + "")
            mGoogleMap = googleMap
            // Add a marker in Sydney and move the camera
            val tapei = LatLng(latitude, longitude)
            val cameraPosition = CameraPosition.Builder().target(tapei).zoom(17f).build()
            mGoogleMap.addMarker(MarkerOptions().position(tapei).title(shopName))
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//            mGoogleMap.uiSettings.isZoomGesturesEnabled = false
            mGoogleMap.uiSettings.setAllGesturesEnabled(false)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}