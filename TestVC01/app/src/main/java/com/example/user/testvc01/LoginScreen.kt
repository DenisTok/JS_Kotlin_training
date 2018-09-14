package com.example.user.testvc01

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val sharedPref = getSharedPreferences("loginData",Context.MODE_PRIVATE)
        val idusers = sharedPref.getString("idusers","")
        val utoken = sharedPref.getString("utoken","")
        Toast.makeText(this, idusers+"  "+utoken,
                Toast.LENGTH_SHORT).show()

    }

    fun toRegForm(view: View){
        // Create an Intent to start the second activity
        val randomIntent = Intent(this, RegScreen::class.java)
        // Start the new activity.
        startActivity(randomIntent)
    }
}
