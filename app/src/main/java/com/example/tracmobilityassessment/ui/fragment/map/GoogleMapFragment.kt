package com.example.tracmobilityassessment.ui.fragment.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.tracmobilityassessment.R
import com.example.tracmobilityassessment.data.model.User
import com.example.tracmobilityassessment.logic.managers.UserManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class GoogleMapFragment : Fragment(), OnMapReadyCallback {
    private val markers = ArrayList<MarkerOptions>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap()

        val user = UserManager.getCurrentUser(requireContext())
        val navigationView = activity?.drawer_layout?.nav_view
        val titlePrefix = requireContext().resources.getString(R.string.nav_profile_title_prefix)
        val title = "${titlePrefix}${user?.fullName}"
        navigationView?.tv_title?.text = title
        navigationView?.tv_email?.text = user?.email
        if (user?.phoneNumber != null)
            navigationView?.tv_phone?.text = user.phoneNumber
        if (user?.imagePhoto != null)
            setProfilePhoto(user, navigationView)
    }

    private fun setProfilePhoto(user: User, navigationView: NavigationView?) {
        Glide.with(this)
            .load(user.imagePhoto)
            .circleCrop()
            .into(navigationView?.iv_profile!!)
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.fragments[0] as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        MapsInitializer.initialize(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        val londonPosition = LatLng(51.51789605104086, -0.1259448985407378)
        val markerPositions = listOf(
            LatLng(51.481962654397364, -0.1118653745911196),
            LatLng(51.51988472421264, -0.17012618293436507),
            LatLng(51.53186064385429, -0.10632364036159234),
            LatLng(51.516116367771716, -0.07258399072988878)
        )
        for (markerPosition in markerPositions) {
            val marker = MarkerOptions()
            marker.icon(bitMapFromVector(R.drawable.ic_marker))
            marker.position(markerPosition)
            markers.add(marker)
            googleMap?.addMarker(MarkerOptions().apply {
                position(markerPosition)
                icon(bitMapFromVector(R.drawable.ic_marker))
                title(getString(R.string.map_marker_title))
            })
        }
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(londonPosition, 12.toFloat()))
    }

    private fun bitMapFromVector(vectorResID: Int): BitmapDescriptor {
        val vectorDrawable =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                ContextCompat.getDrawable(requireContext(), vectorResID)
            else
                AppCompatResources.getDrawable(requireContext(), vectorResID)

        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap =
            Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}