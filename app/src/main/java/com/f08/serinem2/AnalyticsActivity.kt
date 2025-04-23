package com.f08.serinem2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_analytics.*

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)
        Utils(this)
        getData_FromFirebase()
    }

    fun getData_FromFirebase(){
        //addListenerForSingleValueEvent for calling once
        //addValueEventListener for keep updating

        FirebaseDatabase.getInstance().reference.child("data")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(response: DataSnapshot) {
                    //HIDE LOADING AND SHOW ANALYTICS INTERFACE
                    mLoading.visibility = View.GONE
                    analytics_layout.visibility = View.VISIBLE

                    //Create empty object from anaylticsModel
                    val analytics = analyticsModel()

                    // variable from analytics class = value from firebase
                    analytics.Temperature = response.child("Temperature").value as Long
                    analytics.Humidity = response.child("Humidity").value as Long
                    analytics.isNight = response.child("isNight").value as Boolean
                    analytics.isDrySoil = response.child("isDrySoil").value as Boolean
                    analytics.isLedOneLighting = response.child("isLedOneLighting").value as Boolean
                    analytics.isLedTwoLighting = response.child("isLedTwoLighting").value as Boolean
                    analytics.isDoorOpen = response.child("isDoorOpen").value as Boolean
                    analytics.isTrashFull = response.child("isTrashFull").value as Boolean





                    tv_humidity.text = "${analytics!!.Humidity} %"
                    tv_temperature.text = "${analytics.Temperature!!} °C"

                    if(analytics.isNight == true){
                        analytics_background.setBackgroundResource(R.drawable.night)
                    }else{
                        analytics_background.setBackgroundResource(R.drawable.morning)
                    }

                    if (analytics.isDrySoil == true){
                        tv_soil.text = "Sèche"
                    }else{
                        tv_soil.text = "Humide"
                    }

                    if (analytics.isLedOneLighting == true){
                        tv_lamps1.text = "Allumé"
                    }else{
                        tv_lamps1.text = "éteinte"
                    }

                    if (analytics.isLedTwoLighting == true){
                        tv_lamps2.text = "Allumé"
                    }else{
                        tv_lamps2.text = "éteinte"
                    }

                    if (analytics.isDoorOpen == true){
                        tv_door.text = "Ouverte"
                    }else{
                        tv_door.text = "Fermée"
                    }

                    if (analytics.isTrashFull == true){
                        tv_trash.text = "Pleine"
                    }else{
                        tv_trash.text = "Vide"
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }



}
