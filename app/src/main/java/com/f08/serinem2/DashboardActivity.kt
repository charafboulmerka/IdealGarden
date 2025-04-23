package com.f08.serinem2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    lateinit var mUtils:Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mUtils = Utils(this)


        btnControl.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser!=null){
                mUtils.goAnotherActivity(ControlActivity() as Activity)
            }else{
                mUtils.goAnotherActivity(LoginActivity() as Activity)
            }
        }

        btnAnalytics.setOnClickListener {
            mUtils.goAnotherActivity(AnalyticsActivity() as Activity)
        }
    }
}