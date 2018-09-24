package com.example.user.testvc01

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.user.testvc01.R.id.tPass
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.nav_header_main.*


class LoginScreen : AppCompatActivity() {

    private fun isEmailValid(): Boolean {
        val email = tEmail.text.toString()
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val sharedPref = getSharedPreferences("loginData",Context.MODE_PRIVATE)
        val idusers = sharedPref.getInt("idusers",0)
        val utoken = sharedPref.getString("utoken","")

        tEmail.afterTextChanged { tiEmail.error = null }
        tPass.afterTextChanged { tiPass.error = null }


        login(idusers,utoken,"","")
        buLogin.setOnClickListener {
            subButton(it)

        }

    }
    override fun onBackPressed(){
        moveTaskToBack(true)
    }
    fun toRegForm(view: View){
        // Create an Intent to start the second activity
        val randomIntent = Intent(this, RegScreen::class.java)
        // Start the new activity.
        startActivity(randomIntent)
    }
    fun subButton(view: View){
        when {
            !isEmailValid() -> {
                tiEmail.error = "Введите верный email"
            }
            !isPasswordValid() -> {
                tiPass.error = "Введите пароль длинее 7 символов"

            }
            else ->{
                Toast.makeText(this, "Вход",
                        Toast.LENGTH_LONG).show()
                login(0,"",tPass.text.toString(),tEmail.text.toString())
            }
            }
    }



    fun isPasswordValid():Boolean {
        return tPass.text.length > 7
    }

    private fun login(idusers:Int, token:String, password:String, email:String){
        when {
            idusers != 0 && token != "0" -> {
                fuelLoginToken(idusers, token)
            }
            password.length >= 5 && isEmailValid() -> {
                fuelLogin(password,email)
            }
            !isEmailValid() -> {
            }
        }
    }
    fun toMainActivityForm(){
        // Create an Intent to start the UserInformation activity
        val randomIntent = Intent(this, MainActivity::class.java)
        // Start the new activity.
        startActivity(randomIntent)
    }
    private fun doSharedPreferences(user:User){

        val sharedPref = getSharedPreferences("loginData" ,Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("idusers", user.idusers)
            putString("utoken", user.utoken)
            putString("uemail", user.uemail)
            putInt("urole", user.urole)
            apply()
        }
        if(sharedPref.getInt("idusers",0) != 0){
            toMainActivityForm()
        }

    }
    private fun fuelLogin(passeord:String, email:String) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonBody = gson.toJson("")
        Fuel.post(routes.SERVER + routes.LOGIN, listOf("password" to passeord, "uemail" to email)).response { request, response, result ->

            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception.message
                }
                is Result.Success -> {
                    val data = result.get()
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    var user: User = gson.fromJson(String(data, Charsets.UTF_8), User::class.java)
                    doSharedPreferences(user)
                }
            }
        }
    }
    private fun fuelLoginToken(idusers:Int, token:String){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonBody = gson.toJson("")
        Fuel.post(routes.SERVER + routes.LOGINTOKEN,listOf("idusers" to idusers, "utoken" to token)).response { request, response, result ->

            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception.message
                }
                is Result.Success -> {
                    val data = result.get()

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    var user: User = gson.fromJson(String(data, Charsets.UTF_8), User::class.java)
                    doSharedPreferences(user)
                }
            }
        }

    }
    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

}
