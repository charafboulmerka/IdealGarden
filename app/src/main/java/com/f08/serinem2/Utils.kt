package com.f08.serinem2

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.google.firebase.auth.FirebaseAuth


import kotlinx.android.synthetic.main.header.*

class Utils {
    private var mCtx : Context
    private var mAct : Activity
    private var listViews=ArrayList<Int>()
    constructor(mCtx:Context,listViews:ArrayList<Int>?=null) {
        this.mCtx = mCtx
        this.mAct = mCtx as Activity
        if (listViews != null)
            this.listViews = listViews
        if (mAct.header_btn_back != null) {
            mAct.header_btn_back.setOnClickListener { mAct.finish() }
        }

    }

    fun goAnotherActivity(distinationActivity: Activity){
        val intent = Intent(mCtx, distinationActivity::class.java)
        mAct.startActivity(intent)
        mAct.finish()
    }


    fun showLoading(){
        //SHOW LOADING CIRCLE
        val layoutLoading = mAct.findViewById<View>(R.id.layout_loading)
        //MAKE LOADING CIRCLE VISIBLE
        layoutLoading.visibility = View.VISIBLE
        val progressBar = mAct.findViewById<View>(R.id.spin_kit) as ProgressBar
        val doubleBounce: Sprite = Circle()
        progressBar.indeterminateDrawable = doubleBounce

        //Disable all the views (buttons / inputs)
        for (i in listViews){
            val v = mAct.findViewById<View>(i)
            v.isEnabled = false
        }
    }

    fun hideLoading(){
        //HIDE LOADING CIRCLE
        val layoutLoading = mAct.findViewById<View>(R.id.layout_loading)
        //MAKE LOADING CIRCLE INVISIBLE
        layoutLoading.visibility = View.GONE
        val progressBar = mAct.findViewById<View>(R.id.spin_kit) as ProgressBar
        val doubleBounce: Sprite = Circle()
        progressBar.indeterminateDrawable = doubleBounce

        //ENABLE all the views (kima kano)
        for (i in listViews){
            val v = mAct.findViewById<View>(i)
            v.isEnabled = true
        }
    }

    fun SignoutConfirmation(){
        //CREATE ALERT
        val builder = AlertDialog.Builder(mCtx)
        //SET MESSAGE OF THE ALERT
        builder.setMessage("Are you sure you want to sign out ?")
        //ON CLICK YES
        builder.setPositiveButton("Yes") { p0, p1 ->
            try {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(mAct, "You signed out successfully", Toast.LENGTH_LONG).show()
                mAct.finish()
            } catch (e: Exception) {
            }
        }

        builder.setNegativeButton("No") { p0, p1 ->

        }

        builder.create().show()
    }


}