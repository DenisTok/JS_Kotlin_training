package com.example.user.testvc01

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.volMP.u.rel.R
import kotlinx.android.synthetic.main.user_rating_row.view.*


/**
 * Created by brianvoong on 12/18/17.
 */

class RatingAdapter(val rating: List<Rating>): RecyclerView.Adapter<RatingAdapter.CustomViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return rating.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_rating_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        val videoTitle = videoTitles.get(position)
        val rat = rating[position]
        holder.view.tNumber?.text = (position+1).toString()
        holder.view.tName?.text = rat.uiname
        holder.view.tSecName?.text = rat.uisecname
        holder.view.tPoints?.text = rat.sum.toString()

        holder.rating = rat
    }


    class CustomViewHolder(val view: View, var rating: Rating? = null): RecyclerView.ViewHolder(view)



}