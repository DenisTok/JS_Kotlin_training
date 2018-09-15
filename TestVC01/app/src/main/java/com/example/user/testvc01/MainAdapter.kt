package com.example.user.testvc01

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.event_row.view.*


/**
 * Created by brianvoong on 12/18/17.
 */

class MainAdapter(val events: List<Event>): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return events.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.event_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        val videoTitle = videoTitles.get(position)
        val event = events[position]
        holder.view.lEventName?.text = event.ename
        holder.view.tWhen?.text = "Дата: %s".format(event.edate)
        holder.view.tWhere?.text = event.eplace
        holder.view.tPeople?.text = event.epeople
        holder.view.tPoints?.text = event.epoints

        holder?.event = event
    }


    class CustomViewHolder(val view: View, var event: Event? = null): RecyclerView.ViewHolder(view) {
        companion object {
            val EVENT_ID = "1"
            val EVENT_NAME = "2"
            val EVENT_DATE = "3"
            val EVENT_PLACE = "4"
            val EVENT_PEOPLE = "5"
            val EVENT_POINTS = "6"
            val EVENT_INFO = "7"
        }

        init {
            view.setOnClickListener {
                val intent = Intent(view.context, EventInfoScreen::class.java)

                intent.putExtra(EVENT_ID, event?.idevents)
                intent.putExtra(EVENT_NAME, event?.ename)
                intent.putExtra(EVENT_DATE, event?.edate)
                intent.putExtra(EVENT_PLACE, event?.eplace)
                intent.putExtra(EVENT_PEOPLE, event?.epeople)
                intent.putExtra(EVENT_POINTS, event?.epoints)
                intent.putExtra(EVENT_INFO, event?.einfo)


                view.context.startActivity(intent)
            }
        }
    }



}