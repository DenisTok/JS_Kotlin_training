package com.example.user.testvc01

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.user_row.view.*
import org.json.JSONObject


class UsersRowsAdapter(private val users: List<User>): RecyclerView.Adapter<UsersRowsAdapter.CustomViewHolde>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolde {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_row, parent, false)
        return CustomViewHolde(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolde, position: Int) {
            // задаем значение создаваемых полей
        val user = users[position]
        holder.view.tName.text = user.uiname
        holder.view.tSecName.text = user.uisecname
        holder.view.chIsComing.isChecked = user.rverif
        holder.view.tidusersinfo.text = user.idusersinfo.toString()
    }


    class CustomViewHolde(val view: View, var user: User? = null): RecyclerView.ViewHolder(view) {
        init {
            val sharedPrefloginData = view.context.getSharedPreferences("loginData",
                    Context.MODE_PRIVATE)
            val urole = sharedPrefloginData.getInt("urole",0)
            val utoken = sharedPrefloginData.getString("utoken","")

            val sharedPref = view.context.getSharedPreferences("eventData",
                    Context.MODE_PRIVATE)
            val idevent = sharedPref.getInt("idevent",0)

            if (urole == 1){ view.chIsComing.isEnabled = true }
            view.chIsComing.setOnClickListener() {
                //выключаем галочку что бы не отправили дважды случайно
                view.chIsComing.isEnabled = false
                if(view.chIsComing.isChecked){
                    val idusersinfo = view.tidusersinfo.text.toString().toInt()
                    verifUser(utoken, true, idevent,idusersinfo)
                }else{
                    val idusersinfo = view.tidusersinfo.text.toString().toInt()
                    verifUser(utoken, false, idevent, idusersinfo)
                }
            }
        }
        val h = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: android.os.Message) {
                // обновляем TextView
                when {
                    msg.what == 0 -> {
                        view.chIsComing.isChecked = !view.chIsComing.isChecked
                        Toast.makeText(view.context, "Ошибка, попробуйте еще раз",
                                Toast.LENGTH_SHORT).show()
                        view.chIsComing.isEnabled = true
                    }
                    msg.what == 1 -> {
                        view.chIsComing.isChecked = true
                        //включаем галочку если запрос прошел
                        view.chIsComing.isEnabled = true
                    }
                    else -> {

                        view.chIsComing.isChecked = false
                        //включаем галочку если запрос прошел
                        view.chIsComing.isEnabled = true
                    }
                }
            }
        }

        fun verifUser(utoken:String,rverif:Boolean,idevents:Int, idusersinfo:Int?){
            Fuel.post(routes.SERVER + routes.VERIFUSERONEVENT,listOf("utoken" to utoken,
                    "idevents" to idevents,"rverif" to rverif,"idusersinfo" to idusersinfo))
                    .response { request, response, result ->
                        println(request)
                        when (result) {
                            is Result.Failure -> {
                                println("=== Exception51 ===")
                                val ex = result.error.exception.message
                                println(ex)
                                //меняем значение галочки на противоположное и
                                //включаем галочку если запрос не прошел
                                Thread(Runnable {
                                    val i:Int = 0
                                    h.sendEmptyMessage(i)
                                }).start()
                            }
                            is Result.Success -> {
                                val data = result.get()
                                println(String(data, Charsets.UTF_8))
                                println("=== List from JSON ===")
                                val jsonObj = JSONObject(String(data, Charsets.UTF_8))
                                println(jsonObj)
                                println("Boolean" + jsonObj.getBoolean("rverif"))
                                if (jsonObj.getBoolean("rverif")) {
                                    Thread(Runnable {
                                        val i: Int = 1
                                        h.sendEmptyMessage(i)
                                    }).start()
                                }else{
                                    Thread(Runnable {
                                        val i: Int = 2
                                        h.sendEmptyMessage(i)
                                    }).start()

                                }



                            }
                        }
                    }

        }
    }



}