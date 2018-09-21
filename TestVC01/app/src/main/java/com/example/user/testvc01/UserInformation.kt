package com.example.user.testvc01

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_user_information.*
import org.json.JSONObject
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.slots.Slot
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


class UserInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_information)

        val phoneSlots = UnderscoreDigitSlotsParser().parseSlots("+_(___)-___-__-__")

        MaskFormatWatcher(MaskImpl.createTerminated(phoneSlots)).installOn(tPhone)


        buRegIn.setOnClickListener {
            // Handler code here.
            onBuRegInClick()
        }
        tName.afterTextChanged { tiName.error = null
        }
        tSecName.afterTextChanged { tiSecName.error = null }
        tMidName.afterTextChanged { tiMidName.error = null }
        tPhone.afterTextChanged { tiPhone.error = null }
        tSiz.afterTextChanged { tiSiz.error = null }

//        val string = getString(R.string.)

    }

    private fun onBuRegInClick(){
        when{
            //Фамилия
            tSecName.text.isEmpty() -> {
                tiSecName.error = "Введите фамилию"
            }
            tSecName.text.length > 20 -> {
                tiSecName.error = "Слишком длинная фамилия"
            }
            //Имя
            tName.text.isEmpty() -> {
                tiName.error = "Введите имя"
            }
            tName.text.length > 20 -> {
            tiSecName.error = "Слишком длинное имя"
            }
            //Отчество
            tMidName.text.isEmpty() -> {
                tiMidName.error = "Введите отчество"
            }
            tMidName.text.length > 20 -> {
                tiSecName.error = "Слишком длинное отчество"
            }
            //Телефон
            tPhone.text.isEmpty() -> {
                tiPhone.error = "Введите телефон"
            }
            tPhone.text.length < 17 -> {
                tiPhone.error = "Введите верный телефон"
            }
            //Размер одежды
            tSiz.text.isEmpty() -> {
                tiSiz.error = "Введите размер одежды"
            }
            tSiz.text.length > 6 -> {
                tiSiz.error = "Пример размера: L, XL и тд"
            }
            else ->{
                load_uInfo()
            }
        }
    }
    companion object {
        const val uId = ""
    }
    fun getuIdInfo(resp:String):String{
        val jsonObj = JSONObject(resp)
        return jsonObj.getString("idusersinfo")
    }
    fun load_uInfo(){
        val uIdInInfo = intent.getStringExtra(uId)
        val uSecName = tSecName.text.toString()
        val uName = tName.text.toString()
        val uMidName = tMidName.text.toString()
        val uPhone = tPhone.text.toString()
        val uSize = tSiz.text.toString()
        val uInfo = tInfo.text.toString()

        // Instantiate the cache
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }


        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val utoken = sharedPref.getString("utoken","")

        val url = routes.SERVER + routes.REG_uINFO

        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { res ->
                    // success code
                    when {
                        res.toString() == "[3]" ->{
                            Toast.makeText(this, "Информация уже заполнена, проверьте личный кабинет",
                                    Toast.LENGTH_LONG).show()
                            toMainScreenForm()

                        }
                        else->{
                            val myToast = Toast.makeText(this, "response: %s".format(getuIdInfo(res.toString())),
                                    Toast.LENGTH_SHORT)
                            myToast.show()
                            toMainScreenForm()

                        }
                    }

                },
                Response.ErrorListener { er ->
                    // error code
                    val myToast = Toast.makeText(this, "ERROR: %s".format(er.toString()),
                            Toast.LENGTH_SHORT)
                    myToast.show()
                }) {
            override fun getParams(): Map<String, String> = mapOf(
                    "usersIdUsers" to uIdInInfo,
                    "uSecName" to uSecName,
                    "uName" to uName,
                    "uMidName" to uMidName,
                    "uPhone" to uPhone,
                    "uSize" to uSize,
                    "uInfo" to uInfo,
                    "uToken" to utoken)
        }
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)


    }
    fun toMainScreenForm(){
        // Create an Intent to start the UserInformation activity
        val randomIntent = Intent(this, LoginScreen::class.java)
        // Start the new activity.
        startActivity(randomIntent)
    }
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
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
