package com.f08.serinem2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.layout_login.*

class LoginActivity : AppCompatActivity() {
   lateinit var auth :FirebaseAuth
   lateinit var mUtils:Utils

    var layoutViews = arrayListOf<Int>(
        R.id.loginEmail,
        R.id.loginPassword,
        R.id.btnLogin,
        R.id.go_sign_up
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        mUtils= Utils(this,layoutViews);

        btnLogin.setOnClickListener {
            if (loginEmail.text.isEmpty()){
                loginEmail.setError("Please enter your email")
            }else if(loginPassword.text.isEmpty()){
                loginPassword.setError("Please enter your password")
            }else{
                login()
            }

        }


    }



    fun login(){
        mUtils.showLoading()
        auth.signInWithEmailAndPassword(loginEmail.text.toString().trim(),loginPassword.text.toString().trim())
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(this,"Signed in sucessfully",Toast.LENGTH_LONG).show()
                    mUtils.hideLoading()
                    mUtils.goAnotherActivity(ControlActivity() as Activity)

                }else{
                    mUtils.hideLoading()
                    Toast.makeText(this,"Something went wrong please check your data an try again",Toast.LENGTH_LONG).show()
                }
            }

    }

}