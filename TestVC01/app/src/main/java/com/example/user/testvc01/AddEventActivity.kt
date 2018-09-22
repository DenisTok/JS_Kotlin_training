package com.example.user.testvc01

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_add_event.*
import org.json.JSONObject
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

class AddEventActivity : AppCompatActivity() {

    var eispublished:Boolean = false
    var eisarhived:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val isItUpdate:Int = intent.getIntExtra("isAddOrUpdate",0)


        if(isItUpdate == 0){
            buUpdateEvent.visibility = View.INVISIBLE
            buAddEvent.visibility = View.VISIBLE

        }else{
            buUpdateEvent.visibility = View.VISIBLE
            buAddEvent.visibility = View.INVISIBLE
            setInformatioEvent()
        }

        tName.afterTextChanged{tiName.error = null}
        tPlace.afterTextChanged{tiPlace.error = null}
        tDate.afterTextChanged{tiDate.error = null}
        tTime.afterTextChanged{tiTime.error = null}
        tTimeZone.afterTextChanged{tiTimeZone.error = null}
        tInfo.afterTextChanged{tiInfo.error = null}
        tPeople.afterTextChanged{tiPeople.error = null}
        tPoints.afterTextChanged{tiPoints.error = null}
        tPrivat.afterTextChanged{tiPrivat.error = null}

        val tDateSlots = UnderscoreDigitSlotsParser().parseSlots("__-__-____")
        MaskFormatWatcher(MaskImpl.createTerminated(tDateSlots)).installOn(tDate)

        val tTimeSlots = UnderscoreDigitSlotsParser().parseSlots("__:__:__")
        MaskFormatWatcher(MaskImpl.createTerminated(tTimeSlots)).installOn(tTime)

        val tPrivatSlots = UnderscoreDigitSlotsParser().parseSlots("_")
        MaskFormatWatcher(MaskImpl.createTerminated(tPrivatSlots)).installOn(tPrivat)

        sPublish.setOnCheckedChangeListener { _, isChecked ->
            eispublished = isChecked
        }
        sArch.setOnCheckedChangeListener { _, isChecked ->
            eisarhived = isChecked
        }

        tPrivat.setOnClickListener {
            // Handler code here.
            privatedialog()
        }
        tPrivat.setOnFocusChangeListener { v, hasFocus -> if(hasFocus){privatedialog()}}
        buAddEvent.setOnClickListener {
            // Handler code here.
            subButton()
        }
        buUpdateEvent.setOnClickListener {
            // Handler code here.
            subButton()
        }
    }
    fun setInformatioEvent(){
        val sharedPref = getSharedPreferences("eventData", Context.MODE_PRIVATE)
        tName.setText(sharedPref.getString("E_NAME",""))
        tPlace.setText(sharedPref.getString("E_PLACE",""))
        tDate.setText(sharedPref.getString("E_DATE",""))
        tInfo.setText(sharedPref.getString("E_INFO",""))
        tPeople.setText(sharedPref.getString("E_PEOPLE",""))
        tPoints.setText(sharedPref.getString("E_POINTS",""))
        tTime.setText(sharedPref.getString("E_TIME",""))
        tTimeZone.setText(sharedPref.getString("E_TIMEZONE",""))
        tPrivat.setText(sharedPref.getInt("E_PRIVATE",0).toString())
        sPublish.isChecked = sharedPref.getBoolean("E_ISPUBLISHED",false)


    }
    private fun privatedialog(){
        val items = arrayOf<CharSequence>("Публичное", "Пользовательское", "Приватное")
        AlertDialog.Builder(this)
                .setTitle("Выберите приватность события")
                .setItems(items) { dialog, which ->
                    when{
                        items[which] == "Публичное" ->{
                            tPrivat.setText("0")
                        }
                        items[which] == "Пользовательское" ->{
                            tPrivat.setText("1")
                        }
                        items[which] == "Приватное" ->{
                            tPrivat.setText("2")
                        }
                    }
                }.create().show()
    }
    fun toMainActivity(){
        val MainActivityIntent = Intent(this, MainActivity::class.java)
        // Start the new activity.
        startActivity(MainActivityIntent)

    }

    fun subButton(){
        when {
            !tName.text.isNotEmpty() -> {
                tiName.error = "Введите название"
            }
            !tPlace.text.isNotEmpty() -> {
                tiPlace.error = "Введите место"
            }
            !tDate.text.isNotEmpty() -> {
                tiDate.error = "Введите дату"
            }
            !tTime.text.isNotEmpty() -> {
                tiTime.error = "Введите время"
            }
            !tTimeZone.text.isNotEmpty() -> {
                tiTimeZone.error = "Введите временную зону"
            }
            !tInfo.text.isNotEmpty() -> {
                tiInfo.error = "Введите информацию"
            }
            !tPeople.text.isNotEmpty() -> {
                tiPeople.error = "Введите людей"
            }
            !tPoints.text.isNotEmpty() -> {
                tiPoints.error = "Введите баллы"
            }
            !tPrivat.text.isNotEmpty() -> {
                tiPrivat.error = "Введите уровень приватности"
                privatedialog()
            }

            else ->{
                Toast.makeText(this, "Создание",
                        Toast.LENGTH_LONG).show()
                addEvent()
            }
        }

    }
    private fun addEvent(){
//        val event:Event
        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val utoken = sharedPref.getString("utoken","")
        val ename = tName.text.toString()
        val eplace = tPlace.text.toString()
        val edate = tDate.text.toString()
        val etime = tTime.text.toString()
        val etimezone = tTimeZone.text.toString()
        val einfo = tInfo.text.toString()
        val epeople = tPeople.text.toString()
        val epoints = tPoints.text.toString()
        val eprivate = tPrivat.text.toString()
        val eventDataPref = getSharedPreferences("eventData", Context.MODE_PRIVATE)
        val idevent = eventDataPref.getInt("idevent",0)
        val isItUpdate:Int = intent.getIntExtra("isAddOrUpdate",0)
        if(isItUpdate == 0) {
            Fuel.post(routes.SERVER + routes.POSTEVENT, listOf("utoken" to utoken, "ename" to ename,
                    "eplace" to eplace, "edate" to edate, "etime" to etime, "etimezone" to etimezone,
                    "einfo" to einfo, "epeople" to epeople, "epoints" to epoints, "eprivate" to eprivate,
                    "eisarhived" to eisarhived, "eispublished" to eispublished)).response { request, response, result ->

                when (result) {
                    is Result.Failure -> {
                        println("=== Exception ===")
                        val ex = result.error.exception.message
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        println(String(data, Charsets.UTF_8))
                        println("=== List from JSON ===")
                        val jsonObj = JSONObject(String(data, Charsets.UTF_8))
                        println(jsonObj.getString("idevents"))
                        toMainActivity()
                    }
                }
            }
        }else{
            Fuel.post(routes.SERVER + routes.UPDATEEVENT, listOf("utoken" to utoken, "ename" to ename,
                    "eplace" to eplace, "edate" to edate, "etime" to etime, "etimezone" to etimezone,
                    "einfo" to einfo, "epeople" to epeople, "epoints" to epoints, "eprivate" to eprivate,
                    "eisarhived" to eisarhived, "eispublished" to eispublished, "idevents" to idevent)).response { request, response, result ->

                when (result) {
                    is Result.Failure -> {
                        println("=== Exception ===")
                        val ex = result.error.exception.message
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        println(String(data, Charsets.UTF_8))
                        println("=== List from JSON ===")
                        val jsonObj = JSONObject(String(data, Charsets.UTF_8))
                        println(jsonObj.getString("idevents"))
                        toMainActivity()
                    }
                }
            }

        }
    }
    data class Event(val idevents: Int, var ename: String,
                     val eplace: String, val edate: String,
                     val etime: String, val etimezone: String, val einfo: String,
                     val epeople:Int, val epoints:Int, val eprivate:Int,
                     val eisarhived:Boolean, val eispublished:Boolean)
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
