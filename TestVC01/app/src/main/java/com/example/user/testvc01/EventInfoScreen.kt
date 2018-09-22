package com.example.user.testvc01

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_event_info_screen.*
import org.json.JSONObject

class EventInfoScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info_screen)

        sRefrL.isRefreshing = false

        val navBarTitle = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_NAME)
        supportActionBar?.title = navBarTitle

        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val utoken = sharedPref.getString("utoken","")
        val urole = sharedPref.getInt("urole",0)
        val idevent = intent.getIntExtra(MainAdapter.CustomViewHolder.EVENT_ID, 0)

        makeSharedPreferencesOFEvent()

        if(urole == 1){buUpdateEvent.visibility = View.VISIBLE}
        getUsersForEvents(utoken,idevent)
        buRegToEvent.setOnClickListener {
            Toast.makeText(this, "Регистрация",
                    Toast.LENGTH_LONG).show()
            regtoevent(utoken,idevent)
        }
        buUpdateEvent.setOnClickListener {
            toEditEvent()
        }
        sRefrL.setOnRefreshListener {
            onRefresh()
        }
        sRefrL.setColorSchemeColors( Color.RED, Color.GREEN, Color.BLUE, Color.CYAN)


        tName.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_NAME)
        tWhere.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PLACE)
        tDate.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_DATE)
        tInfo.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_INFO)
        tPeople.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PEOPLE)
        tPoints.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_POINTS)
    }
    fun onRefresh(){
        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val utoken = sharedPref.getString("utoken","")
        val idevent = intent.getIntExtra(MainAdapter.CustomViewHolder.EVENT_ID, 0)

        makeSharedPreferencesOFEvent()
        
        getUsersForEvents(utoken,idevent)
        sRefrL.isRefreshing = false
    }
    fun toEditEvent(){
        val AddEventActivityIntent = Intent(this, AddEventActivity::class.java)
        AddEventActivityIntent.putExtra("isAddOrUpdate", 1)
        // Start the new activity.
        startActivity(AddEventActivityIntent)
    }
    fun makeSharedPreferencesOFEvent(){
        val sharedPref = getSharedPreferences("eventData" ,Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("idevent", intent.getIntExtra(MainAdapter.CustomViewHolder.EVENT_ID,0))
            putString("E_NAME", intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_NAME))
            putString("E_PLACE",  intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PLACE))
            putString("E_DATE", intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_DATE))
            putString("E_INFO", intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_INFO))
            putString("E_PEOPLE", intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PEOPLE))
            putString("E_POINTS", intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_POINTS))
            putString("E_TIME", intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_TIME))
            putString("E_TIMEZONE", intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_TIMEZONE))
            putInt("E_PRIVATE", intent.getIntExtra(MainAdapter.CustomViewHolder.EVENT_PRIVATE,0))
            putBoolean("E_ISPUBLISHED", intent.getBooleanExtra(MainAdapter.CustomViewHolder.EVENT_ISPUBLISHED,false))

            apply()
        }

    }
    fun regtoevent(utoken:String, idevents:Int){

        Fuel.post(routes.SERVER + routes.REGONEVENT,listOf("utoken" to utoken, "idevents" to idevents))
                .response { request, response, result ->

            when (result) {
                is Result.Failure -> {
                    println("=== Exception ===")
                    val ex = result.error.exception.message
                    println(ex)
                    Toast.makeText(this, "Error: %s".format(ex),
                            Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    Toast.makeText(this, "Успешно",
                            Toast.LENGTH_SHORT).show()
                    val data = result.get()
                    println(String(data, Charsets.UTF_8))
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    println("=== List from JSON ===")
                    val jsonObj = JSONObject(String(data, Charsets.UTF_8))
                    println(jsonObj)

                }
            }
        }

    }


    fun getUsersForEvents(utoken:String,idevents:Int ){


        fuelSeeUsers(utoken,idevents)


    }

    private fun fuelSeeUsers(token:String, idevents:Int){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonBody = gson.toJson("")
        val recyclerView: RecyclerView = findViewById(R.id.rvEventInfo)
        recyclerView.layoutManager = LinearLayoutManager(this)

        Fuel.post(routes.SERVER + routes.GETUSERSONEVENT,listOf("utoken" to token, "idevents" to idevents)).response { request, response, result ->

            when (result) {
                is Result.Failure -> {
                    println("=== Exception ===")
                    val ex = result.error.exception.message
                    println(ex)
                }
                is Result.Success -> {
                    val data = result.get()

                    println(String(data, Charsets.UTF_8))
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    println("=== List from JSON ===")
                    var userList: List<User> = gson.fromJson(String(data, Charsets.UTF_8), object : TypeToken<List<User>>() {}.type)
                    userList.forEach { println(it) }

                    runOnUiThread {
                        recyclerView.adapter = UsersRowsAdapter(userList)
                    }


                }
            }
        }

    }
}

