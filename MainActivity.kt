package com.example.healthcare.Activities

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    class MyClass{
        companion object{
            @SuppressLint("StaticFieldLeak")
            var activity: Activity? = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.healthcare.R.layout.activity_main)

//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//            .addOnCompleteListener { task ->
//                var msg = "Done"
//                if (!task.isSuccessful) {
//                    msg = "Failed"
//                }
////                Log.d(TAG, msg!!)
//                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
//            }
//
        MyClass.activity = this@MainActivity

    }

    override fun onBackPressed() {
        super.onBackPressed()
        PatientActivity.MyClass.activity?.finish()
        ClinicalActivity.MyClass.activity?.finish()
    }
}