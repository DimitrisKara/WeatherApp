package com.cammace.aurora

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cammace.aurora.ui.MainActivity
import com.cammace.aurora.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_coordinates.*

class coordinates : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinates)


        accept.setOnClickListener(
        object : View.OnClickListener {
            override fun onClick(v: View) {
                var latitudeS: String? = latitude.getText().toString()
                var longtitudeS: String? = longitude.getText().toString()
                if ((latitude.getText().toString().isEmpty() || longitude.getText().toString().isEmpty())) {
                    val builder = AlertDialog.Builder(this@coordinates)
                            builder.setTitle("Error")
                            builder.setMessage("Παρακαλώ συμπληρώστε τις συντεταγμένες")
                            builder.setNegativeButton("Ok", null)
                            builder.show()
                } else if (latitudeS != null) {
                    if (latitudeS.length - latitudeS.replace(".", "").length > 1) {
                        Toast.makeText(getApplicationContext(), "Παρακαλώ εισάγετε μόνο μία τελεία", Toast.LENGTH_SHORT).show()
                    } else if (longtitudeS != null) {
                        if (longtitudeS.length - longtitudeS.replace(".", "").length > 1) {
                            Toast.makeText(getApplicationContext(), "Παρακαλώ εισάγετε μόνο μία τελεία", Toast.LENGTH_SHORT).show()
                        } else {
                            val GetLat: String = latitude.getText().toString()
                            val GetLong: String = longitude.getText().toString()
                            val intent = Intent(this@coordinates, MainActivity::class.java)
                            intent.putExtra("Lat", GetLat)
                            intent.putExtra("Long", GetLong)
                            startActivity(intent)
                            latitude.setText("")
                            longitude.setText("")
                        }
                    }
                }
            }
        })
    }
}
