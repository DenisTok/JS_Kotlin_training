package com.example.user.testvc01

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.example.user.testvc01.R.id.*
import kotlinx.android.synthetic.main.activity_reg_screen.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern
import android.text.TextUtils




class RegScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_screen)
        tEmail.afterTextChanged { tiEmail.error = null }
        tPass.afterTextChanged { tiPass.error = null }
        tRePass.afterTextChanged {
            if(isPasswordsTheSame()) {
            tiRePass.error = null
        }
        }



    }

    private fun isEmailValid(): Boolean {
        val email = tEmail.text.toString()
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid():Boolean {
        return tPass.text.length > 7
    }

    fun isPasswordsTheSame():Boolean{
        return tPass.text.toString() == tRePass.text.toString()
    }
    fun subButton(view: View){
        when {
            !isEmailValid() -> {
                tiEmail.error = "Введите верный email"
            }
            !isPasswordValid() -> {
                tiPass.error = "Введите пароль длинее 7 символов"

            }
            !isPasswordsTheSame() -> {
                tiRePass.error = "Пароли не совпадают"

            }
            else ->{
                Toast.makeText(this, "Регистрация",
                        Toast.LENGTH_LONG).show()
                tryToReg(view)
        }
        }

    }
    fun tryToReg(view: View){
        val email = tEmail.text.toString()
        val password = tPass.text.toString()

        // Instantiate the cache
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val url = routes.SERVER + routes.uREG

        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { res ->
                    // success code
                    when {
                        res.toString() == "[0]" ->{
                            Toast.makeText(this, "Ошибка: Попробуйте еще раз через некоторое время",
                                    Toast.LENGTH_LONG).show()

                        }
                        res.toString() == "[1]" ->{
                            Toast.makeText(this, "Ошибка: Попробуйте еще раз через некоторое время",
                                    Toast.LENGTH_LONG).show()
                        }
                        res.toString() == "[2]" ->{
                            Toast.makeText(this, "Ошибка: Пользователь с такой почтой уже существует",
                                    Toast.LENGTH_LONG).show()
                        }
                        res.toString() == "[1][2]" ->{
                            Toast.makeText(this, "Ошибка: Почта введена не правильно",
                                    Toast.LENGTH_LONG).show()
                        }
                        res.toString() == "[0][2]" ->{
                            Toast.makeText(this, "Ошибка: Попробуйте еще раз через некоторое время",
                                    Toast.LENGTH_LONG).show()
                        }

                        else->{
                            println(res)
                            val jsonObj = JSONObject(res)
                            val myToast = Toast.makeText(this, "response: %s".format(jsonObj.getString("idusers")),
                                    Toast.LENGTH_SHORT)
                            myToast.show()

                            val sharedPref = getSharedPreferences("loginData" ,Context.MODE_PRIVATE)
                            with (sharedPref.edit()) {
                                putInt("idusers", jsonObj.getInt("idusers"))
                                putString("utoken", jsonObj.getString("utoken"))
                                putString("uemail", email)
                                putInt("urole", 0)
                                apply()
                            }

                            toUserInformationForm(jsonObj.getString("idusers"))

                        }
                    }

                },
                Response.ErrorListener { er ->
                    // error code
                    val myToast = Toast.makeText(this, "ERROR: %s".format(er.toString()),
                            Toast.LENGTH_SHORT)
                    myToast.show()

                }) {
            override fun getParams(): Map<String, String> = mapOf("email" to email,"password" to password)
            }
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)

    }

    fun toUserInformationForm(uId:String){
        // Create an Intent to start the UserInformation activity
        val randomIntent = Intent(this, UserInformation::class.java)
        // Add the uId to the extras for the Intent.
        randomIntent.putExtra(UserInformation.uId, uId)
        // Start the new activity.
        startActivity(randomIntent)
    }
    private fun saveInInternalFolder(stringToSave:String,fileName:String){
         val fos: FileOutputStream
        try{
            fos = openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write(stringToSave.toByteArray())
            fos.close()

        }catch (e:IOException){
            Toast.makeText(this, "ERROR: %s".format(e.toString()),
                    Toast.LENGTH_SHORT)
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
