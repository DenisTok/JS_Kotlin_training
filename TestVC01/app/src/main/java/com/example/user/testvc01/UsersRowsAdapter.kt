package com.example.user.testvc01

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.user_row.view.*


class UsersRowsAdapter(val users: List<User>): RecyclerView.Adapter<UsersRowsAdapter.CustomViewHolde>() {

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
    }


    class CustomViewHolde(val view: View, var user: User? = null): RecyclerView.ViewHolder(view) {
        companion object {
            val USER_IDUSERS = "1"
            val USER_IDUSERSINFO = "2"
            val USER_NAME = "2"
            val USER_SECNAME = "3"
        }

        init {
            val sharedPref = view.context.getSharedPreferences("loginData", Context.MODE_PRIVATE)
            val urole = sharedPref.getInt("urole",0)
            if (urole == 1){ view.chIsComing.isEnabled = true }
            view.chIsComing.setOnClickListener {
                Toast.makeText(view.context, "This is USER",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }



}