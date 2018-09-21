package com.example.user.testvc01

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_event_info_screen.*
import org.json.JSONObject

class EventInfoScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info_screen)

        val navBarTitle = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_NAME)
        supportActionBar?.title = navBarTitle

        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val idusers = sharedPref.getInt("idusers",0)
        val utoken = sharedPref.getString("utoken","")
        val urole = sharedPref.getInt("urole",0)
        val idevent = intent.getIntExtra(MainAdapter.CustomViewHolder.EVENT_ID, 0)

        if(urole == 1){buUpdateEvent.visibility = View.VISIBLE}

        buRegToEvent.setOnClickListener {
            Toast.makeText(this, "Регистрация",
                    Toast.LENGTH_LONG).show()
            regtoevent(utoken,idevent)
        }



        tName.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_NAME)
        tWhere.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PLACE)
        tDate.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_DATE)
        tInfo.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_INFO)
        tPeople.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PEOPLE)
        tPoints.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_POINTS)
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


    fun getUsersForEvents( idusers:Int,utoken:String,idevent:String ){
        val recyclerView: RecyclerView = findViewById(R.id.rvEventInfo)
        recyclerView.layoutManager = LinearLayoutManager(this)


        fuelSeeUsers(idusers,utoken,idevent)

        recyclerView.adapter = UsersRowsAdapter()
    }

    private fun fuelSeeUsers(idusers:Int, token:String, idevent:String){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonBody = gson.toJson("")
        Fuel.post(routes.SERVER + routes.LOGINTOKEN,listOf("idusers" to idusers, "utoken" to token, "idevent" to idevent)).response { request, response, result ->

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
                    var user: User = gson.fromJson(String(data, Charsets.UTF_8), User::class.java)
                    println("fuelLoginToken: " + user)

                }
            }
        }

    }
}

