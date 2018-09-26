package com.example.user.testvc01

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.github.kittinunf.fuel.httpGet
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.content_main.*
import android.content.Intent
import android.view.View
import app.volMP.u.rel.R


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        swipeRefrL.isRefreshing = true
        isUserModer()
        fab.setOnClickListener { view ->
            toAddEventActivity()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // SwipeRefreshLayout
        swipeRefrL.setOnRefreshListener {
            onRefresh()
        }
        swipeRefrL.setColorSchemeColors( Color.RED, Color.GREEN, Color.BLUE, Color.CYAN)


    }
    fun isUserModer(){
        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val urole = sharedPref.getInt("urole",0)
        if (urole == 1){ fab.visibility = View.VISIBLE }

    }
    fun toAddEventActivity(){
        // Create an Intent to start the UserInformation activity
        val AddEventActivityIntent = Intent(this, AddEventActivity::class.java)
        // Start the new activity.
        startActivity(AddEventActivityIntent)
    }
    override fun onStart() {
        httpGet()
        swipeRefrL.isRefreshing = false
        super.onStart()
    }
    private fun onRefresh() {
        // Fetching data from server
        httpGet()
        swipeRefrL.isRefreshing = false

    }


    private fun httpGet() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView_main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val url:String = routes.SERVER+routes.GET_EVENTS

        url.httpGet().responseString { request, response, result ->
            //do something with response
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()

                }
                is Result.Success -> {
                    val data = result.get()
                    val gson = GsonBuilder().setPrettyPrinting().create()

                    var eventList: List<Event> = gson.fromJson(data.trimMargin(), object : TypeToken<List<Event>>() {}.type)

                    runOnUiThread {
                        recyclerView.adapter = MainAdapter(eventList)
                    }

                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            moveTaskToBack(true)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                AlertDialog.Builder(this)
                        .setTitle("Создатель")
                        .setMessage("Разработал Токарев Денис, Контакты email: tokd@tuta.io")
                        .setPositiveButton(android.R.string.yes) { arg0, arg1 ->
                        }.create().show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_acc -> {
                val AccInformationIntent = Intent(this, AccInformation::class.java)
                // Start the new activity.
                startActivity(AccInformationIntent)

            }
            R.id.nav_top -> {
                val ActivityRatingIntent = Intent(this, ActivityRating::class.java)
                // Start the new activity.
                startActivity(ActivityRatingIntent)

            }
            R.id.nav_exite -> {
                // Create an Intent to start the UserInformation activity
                val LoginScreenIntent = Intent(this, LoginScreen::class.java)
                // Start the new activity.
                startActivity(LoginScreenIntent)

                val loginDataPref = getSharedPreferences("loginData" ,Context.MODE_PRIVATE)
                with (loginDataPref.edit()) {
                    remove("idusers")
                    remove("utoken")
                    remove("uemail")
                    remove("urole")
                    apply()
                }


            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
