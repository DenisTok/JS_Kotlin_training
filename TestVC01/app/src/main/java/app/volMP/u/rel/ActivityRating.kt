package app.volMP.u.rel

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_rating.*

class ActivityRating : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        getRating()
        sssRefrL.setOnRefreshListener {
            getRating()
            sssRefrL.isRefreshing = false
        }
        sssRefrL.setColorSchemeColors( Color.RED, Color.GREEN, Color.BLUE, Color.CYAN)

    }

    private fun getRating(){
        val recyclerView: RecyclerView = findViewById(R.id.rwRating)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val utoken = sharedPref.getString("utoken","")


        Fuel.post(routes.SERVER + routes.GETTOP, listOf("utoken" to utoken))
                .response { _, _, result ->

            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception.message
                    println(ex)
                }
                is Result.Success -> {
                    val data = result.get()
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val ratingList: List<Rating> = gson.fromJson(String(data, Charsets.UTF_8), object : TypeToken<List<Rating>>() {}.type)
                    runOnUiThread {
                        recyclerView.adapter = RatingAdapter(ratingList)
                    }
                }
            }
        }

    }
}
