package com.cammace.aurora.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cammace.aurora.*
import com.cammace.aurora.databinding.ActivityMainBinding
import com.cammace.aurora.utils.WeatherIcons
import com.cammace.aurora.viewmodel.MainActivityViewModel
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*



const val REQUEST_COARSE_LOCATION = 5678

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var viewModel: MainActivityViewModel

  private var weatherIconMap: Map<String, Drawable>? = null

  // Places autocomplete fragment
  private var placeAutoCompleteFragment: PlaceAutocompleteFragment? = null

  // 5 day forecast adapter
  private val adapter = DailyForecastAdapter()

  //for DB
  var myDb: DatabaseHelper? = null
  var btnSaveData: Button? = null
  var btnviewAll: Button? = null
  var Temperature: TextView? = null
  var Weather: TextView? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    addObservers()

    // Setup toolbar and hide title
    setSupportActionBar(toolbar_mainActivity)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    weatherIconMap = WeatherIcons.map(this)

    var Latitude: String?
    var Longtitude: String?

    Latitude = intent.getStringExtra("Lat")
    Longtitude = intent.getStringExtra("Long")

    if(Latitude != null && Longtitude != null ){
      viewModel.useCoordinates(Latitude.toDouble(),Longtitude.toDouble())
    }
    else {
      viewModel.getUsersCurrentLocation()
    }

    recyclerView_mainActivity_dailyForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    recyclerView_mainActivity_dailyForecast.adapter = adapter

    //onCreate from DB

    myDb = DatabaseHelper(this)
    Temperature = findViewById<TextView>(R.id.textView_mainActivity_temp)
    Weather = findViewById<TextView>(R.id.textView_mainActivity_summary)
    btnSaveData = findViewById<View>(R.id.button_save) as Button
    btnviewAll = findViewById<View>(R.id.button_viewAll) as Button
    saveData()
    viewAll()
  }

  private fun saveData() {
    btnSaveData!!.setOnClickListener(
            View.OnClickListener {
              val date = Date()
              val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
              val isInserted = myDb!!.insertData(formatter.format(date),Temperature?.text.toString(),Weather?.text.toString(),binding.locationName)
              if (isInserted == true)
                  Toast.makeText(this@MainActivity, "Data Inserted", Toast.LENGTH_LONG).show()
              else
                  Toast.makeText(this@MainActivity, "Data not Inserted", Toast.LENGTH_LONG).show()
            }
    )
  }

  fun viewAll() {
    btnviewAll!!.setOnClickListener(
            View.OnClickListener {
              val res = myDb!!.allData
              if (res.count == 0) { // show message
                showMessage("Error", "Nothing found")
                return@OnClickListener
              }
              val buffer = StringBuffer()
              while (res.moveToNext()) {
                buffer.append("Id : " + res.getString(0) + "\n")
                buffer.append("Date : " + res.getString(1) + "\n")
                buffer.append("Temperature : " + res.getString(2) + "\n")
                buffer.append("Weather : " + res.getString(3) + "\n")
                buffer.append("City : " + res.getString(4) + "\n\n")
              }
              // Show all data
              showMessage("Data", buffer.toString())
            }
    )
  }

  private fun showMessage(title: String?, Message: String?) {
    val builder = AlertDialog.Builder(this)
    builder.setCancelable(true)
    builder.setTitle(title)
    builder.setMessage(Message)
    builder.show()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_toolbar, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.action_search) {
      displayPlacesFragment()
    } else if (item?.itemId == R.id.action_user_location) {
      viewModel.getUsersCurrentLocation()
    }
    else if (item?.itemId == R.id.action_about){
      startActivity(Intent(this@MainActivity, AboutActivity::class.java))
    }
    else {
      displayPlacesFragmentTest()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    if (placeAutoCompleteFragment != null && placeAutoCompleteFragment!!.isVisible) {
      hidePlacesFragment()
      return
    }
    super.onBackPressed()
  }

  private fun displayPlacesFragment() {
    if (placeAutoCompleteFragment == null) {

      val options = PlaceOptions.builder()
        .toolbarColor(ContextCompat.getColor(this, R.color.materialGray_50))
        .backgroundColor(ContextCompat.getColor(this, R.color.materialGray_50))
        .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_REGION)
        .build()

      window.statusBarColor = ContextCompat.getColor(this, R.color.materialGray_500)
      placeAutoCompleteFragment = PlaceAutocompleteFragment.newInstance(BuildConfig.MAPBOX_ACCESS_TOKEN, options)
      placeAutoCompleteFragment!!.setOnPlaceSelectedListener(viewModel)

      val transaction = supportFragmentManager.beginTransaction()
      transaction.add(R.id.fragment_container, placeAutoCompleteFragment!!, PlaceAutocompleteFragment.TAG)
      transaction.commit()
    } else {

      showPlacesFragment()
    }
  }

  private fun showPlacesFragment() {
    window.statusBarColor = ContextCompat.getColor(this, R.color.materialGray_500)
    supportFragmentManager.beginTransaction().show(placeAutoCompleteFragment!!).commit()
  }

  //test
  private fun displayPlacesFragmentTest() {
    val intent = Intent(this, coordinates::class.java)
    startActivity(intent)
  }

  private fun hidePlacesFragment() {
    window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    val inputMethod = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.hideSoftInputFromWindow(textView_mainActivity_wind.windowToken, 0)
    supportFragmentManager.beginTransaction().hide(placeAutoCompleteFragment!!).commit()
  }

  private fun addObservers() {
    viewModel.requestLocationPermissionLiveData.observe(this, Observer { shouldRequestPermission ->
      if (shouldRequestPermission) {
        Timber.v("requesting permission")
        requestPermissions()
      }
    })

    viewModel.locationNameLiveData.observe(this, Observer { locationName ->
      binding.locationName = locationName
    })

    viewModel.darkSkyApiResponseLiveData.observe(this, Observer { darkSkyModel ->
      binding.currentCondition = darkSkyModel.currently

      adapter.setDayForecast(darkSkyModel.daily.data)

      // Bind the current weather icon
      if (darkSkyModel.currently.icon != null && weatherIconMap != null) {
        binding.currentConditionIcon = weatherIconMap!![darkSkyModel.currently.icon]
      }
    })

    viewModel.userFinishedSearchLiveData.observe(this, Observer { canceled ->
      Timber.v("User has canceled geocoding search.")
      if (canceled && placeAutoCompleteFragment != null) {
        hidePlacesFragment()
      }
    })
  }

  private fun requestPermissions() {
    ActivityCompat.requestPermissions(this,
      arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == REQUEST_COARSE_LOCATION && grantResults.isNotEmpty()
      && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Timber.v("User gave location permission, continue with getting user's last location.")
      viewModel.getUsersCurrentLocation()
    } else {
      Timber.v("User refused to give location permission. Continue using the default location.")
    }
  }
}
