package com.example.prm4

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.prm4.databinding.MapBinding
import com.example.prm4.fragments.EditFragment
import com.example.prm4.fragments.InfoFragment
import com.example.prm4.fragments.ListFragment
import com.example.prm4.photo.fragment.PaintFragment
import com.example.prm4.photo.fragment.SettingsFragment
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.Locale

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment
    lateinit var binding: MapBinding

    private var isUserInsettings = false
    private val paintFragment
        get() = supportFragmentManager.findFragmentByTag(PaintFragment::class.java.name) as? PaintFragment
    private val settingsFragment
        get() = supportFragmentManager.findFragmentByTag(SettingsFragment::class.java.name) as? SettingsFragment





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.actyvity_main)


        listFragment = ListFragment()


        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()



        if(checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ){
                if(it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true){
                    locationOn()
                }
            }.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            locationOn()
        }



    }

    @SuppressLint("MissingPermission")
    private fun locationOn() {

        binding.map.apply {
            overlays.add(
                MyLocationNewOverlay(this).apply {
                    enableMyLocation()
                }
            )
            getSystemService(LocationManager::class.java)
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses: List<Address>? = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address: Address = addresses[0]
                        val city: String? = address.locality
                        val country: String? = address.countryName
                        val street: String? = address.thoroughfare

                        // Przypisanie miasta, kraju i ulicy do tekstów w aplikacji
                        val cityText = "${city}"
                        val countryText = "${country}"
                        val streetText = "${street}"
                        val result = "ul. " + streetText + ", " + cityText + ", " + countryText
                        EditFragment.setAd(result)
                    }

                }
        }
    }

    override fun navigate(to: Navigable.Destination) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> replace(R.id.container, listFragment, listFragment.javaClass.name)
                Navigable.Destination.Add -> {
                    replace(R.id.container, EditFragment(), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Edit -> {
                    replace(R.id.container, EditFragment(), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Info -> {
                    replace(R.id.container, InfoFragment(), InfoFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Photo -> {
                    replace(R.id.container, SettingsFragment(), SettingsFragment::class.java.name)
                        .hide(SettingsFragment())
                        .add(R.id.container, PaintFragment(), PaintFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)

                }

            }
        }.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.settings -> {
                supportFragmentManager.beginTransaction().apply {
                    if(isUserInsettings){
                        settingsFragment?.let{ hide(it) }
                        paintFragment?.let{ show(it) }
                    } else {
                        settingsFragment?.let{ show(it) }
                        paintFragment?.let{ hide(it) }
                    }
                    isUserInsettings = !isUserInsettings
                    commit()
                }
                true
            }

            androidx.compose.ui.R.id.compose_view_saveable_id_tag -> {
                paintFragment?.save()

                navigate(Navigable.Destination.Add)
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }

}

