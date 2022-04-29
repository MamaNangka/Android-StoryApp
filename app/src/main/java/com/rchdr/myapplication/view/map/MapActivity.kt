package com.rchdr.myapplication.view.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.rchdr.myapplication.R
import com.rchdr.myapplication.api.RetrofitApiConfig
import com.rchdr.myapplication.data.model.UserPreference
import com.rchdr.myapplication.data.response.ListStoryItem
import com.rchdr.myapplication.data.response.StoryResp
import com.rchdr.myapplication.data.viewmodel.AllViewModel
import com.rchdr.myapplication.data.viewmodel.ViewModelFactory
import com.rchdr.myapplication.databinding.ActivityMapBinding
import com.rchdr.myapplication.view.add.AddStoryActivity
import com.rchdr.myapplication.view.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var MapBinding: ActivityMapBinding
    private lateinit var MapViewModel: AllViewModel
    private lateinit var MapLocationClient: FusedLocationProviderClient

    private val _MapLocation = MutableLiveData<List<ListStoryItem>>()
    private val MapLocation: LiveData<List<ListStoryItem>> = _MapLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(MapBinding.root)
        supportActionBar?.hide()

        setupViewModel()
        getStoriesLocation()
        MapLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val fab: View = findViewById(R.id.fab_post_location)
        fab.setOnClickListener { _ ->
            val it = Intent(this, AddStoryActivity::class.java)
            it.putExtra(AddStoryActivity.lat, lat.toFloat())
            it.putExtra(AddStoryActivity.lng, lng.toFloat())

            startActivity(it)

        }

    }

    private fun setupViewModel() {
        MapViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AllViewModel::class.java]
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                else -> {
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            MapLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mMap.isMyLocationEnabled = true

                    showMyLocationMarker(location)
                } else {
                    Toast.makeText(
                        this@MapActivity,
                        getString(R.string.location_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showMyLocationMarker(location: Location) {
        lat = location.latitude
        lng = location.longitude

        val startLocation = LatLng(lat, lng)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .draggable(true)
                .title(getString(R.string.location))
        )
    }

    private fun getStoriesLocation() {
        MapViewModel.getUser().observe(this) {
            if (it != null) {
                val client = RetrofitApiConfig.getApiService().getStoryLocation("Bearer " + it.token)
                client.enqueue(object: Callback<StoryResp> {
                    override fun onResponse(
                        call: Call<StoryResp>,
                        response: Response<StoryResp>
                    ) {
                        val responseBody = response.body()
                        if (response.isSuccessful && responseBody?.message == "Stories fetched successfully") {
                            _MapLocation.value = responseBody.listStory
                        }
                    }

                    override fun onFailure(call: Call<StoryResp>, t: Throwable) {
                        Toast.makeText(
                            this@MapActivity,
                            getString(R.string.story_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            }
        }
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
            if (!success) {
                showToast(this, getString(R.string.map_style_error))
            }
        } catch (exception: Resources.NotFoundException) {
            showToast(this, getString(R.string.map_style_error))
        }
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        getMyLocation()

        val pekanbaru = LatLng(0.51, 101.43)

        MapLocation.observe(this) {
            for(i in MapLocation.value?.indices!!) {
                val location = LatLng(MapLocation.value?.get(i)?.lat!!, MapLocation.value?.get(i)?.lon!!)
                mMap.addMarker(MarkerOptions().position(location).title(getString(R.string.location_map) + MapLocation.value?.get(i)?.lat +", "+ MapLocation.value?.get(i)?.lon + " " + getString(R.string.name_map) + " " + MapLocation.value?.get(i)?.name))
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pekanbaru, 5f))
        }
    }


    companion object {
        var lat = 0.0
        var lng = 0.0
    }


}