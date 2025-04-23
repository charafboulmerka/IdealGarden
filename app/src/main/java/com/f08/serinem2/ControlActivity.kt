package com.f08.serinem2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_control.*
import kotlinx.android.synthetic.main.header_control.*


class ControlActivity : AppCompatActivity() {
    var IP = ""
    lateinit var mUtils: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
         mUtils = Utils(this)

        header_btn_logout.setOnClickListener {
            mUtils.SignoutConfirmation()
        }

        getIP_FromFirebase()





        switch_auto.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                if (isOn) {
                    makeRequest("auto","off")
                }else{
                    makeRequest("auto","on")
                }
                recreate()

            }

        })


        switch_water.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                //AFTER CLICKING ON THE SWITCH
                if (isOn) {
                    makeRequest("water","on")
                }else{
                    makeRequest("water","off")
                }

            }

        })


        switch_door.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                if (isOn) {
                    makeRequest("door","on")
                }else{
                    makeRequest("door","off")
                }

            }

        })

        switch_lamps1.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                if (isOn) {
                    makeRequest("lamps1","on")
                }else{
                    makeRequest("lamps1","off")
                }
                switch_all_lamps.isOn = switch_lamps2.isOn && isOn

            }

        })

        switch_lamps2.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                if (isOn) {
                    makeRequest("lamps2","on")

                }else{
                    makeRequest("lamps2","off")
                }
                switch_all_lamps.isOn = switch_lamps1.isOn && isOn

            }

        })


        switch_all_lamps.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                if (isOn) {
                    makeRequest("all_lamps","on")
                }else{
                    makeRequest("all_lamps","off")
                }
                switch_lamps1.isOn = isOn
                switch_lamps2.isOn = isOn

            }

        })




    }

    fun getIP_FromFirebase(){

        FirebaseDatabase.getInstance().reference.child("data")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(analyticsModel::class.java)

                    //SET IP THAT WE GOT FROM THE FIREBASE
                    IP = data!!.IP!!.toString()
                    initView()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    fun initView(){
        //http://192.168.1.36/analyticsMobile
        AndroidNetworking.get("http://$IP/analyticsMobile")
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    //response -> WE GOT RESPONSE FROM THE ESP

                    //HIDE LOADING AND SHOW CONTROL INTERFACE
                    mLoading.visibility = View.GONE
                    control_layout.visibility = View.VISIBLE

                    //STORE ALL THE DATA ON THE VARIABLE analytics
                    //hezina réponse 7tinaha fel model ta3na (analyticsModel) w khbinaha fel variable analytics
                    val analytics = Gson().fromJson(response, analyticsModel::class.java)


                    //init les switch ta3na b les valeur ta3 dork
                    switch_auto.isOn = analytics.isManually!!
                    switch_water.isOn = analytics.isWaterON!!
                    switch_door.isOn = analytics.isDoorOpen!!
                    switch_lamps1.isOn = analytics.isLedOneLighting!!
                    switch_lamps2.isOn = analytics.isLedTwoLighting!!
                    switch_all_lamps.isOn = analytics.isLedOneLighting!! && analytics.isLedTwoLighting!!

                    //DISABLE ALL SWITCHS IF CONTROL IS AUTOMATIQUE
                    if (analytics.isManually!!){
                        switchsState(true)
                    }else{
                        switchsState(false)
                    }

                    if(analytics.isNight!!){
                        main_background.setBackgroundResource(R.drawable.night)
                    }else{
                        main_background.setBackgroundResource(R.drawable.morning)
                    }



                }

                override fun onError(anError: ANError) {

                }
            })
    }

    fun switchsState(state:Boolean){
        switch_water.isEnabled = state
        switch_door.isEnabled = state
        switch_lamps1.isEnabled = state
        switch_lamps2.isEnabled = state
        switch_all_lamps.isEnabled = state
    }


    fun makeRequest(element:String,state:String){
        //in the reality aw yweli maktoub haka "http://192.168.1.36/water/on" for exemple
        //GET REQUEST
        AndroidNetworking.get("http://$IP/$element/$state")
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                }

                override fun onError(anError: ANError) {
                }
            })
    }

}