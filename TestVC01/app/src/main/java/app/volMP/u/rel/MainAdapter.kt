package app.volMP.u.rel

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
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
        holder.view.tTime?.text = "Время: %s".format(event.etime)
        holder.view.tWhere?.text = event.eplace
        holder.view.tPeople?.text = "Нужно людей: %s".format(event.epeople)
        holder.view.tPoints?.text = "Баллов: %s".format(event.epoints)
        holder.view.eventidtext?.text = event.idevents.toString()
        when{
            event.eisarhived -> {
                holder.view.lEventName?.setBackgroundColor(Color.argb(128,255,191,128))
            }
            !event.eispublished -> {
                holder.view.lEventName?.setBackgroundColor(Color.argb(128,255,255,102))
            }
            event.eprivate == 2 -> {
                holder.view.lEventName?.setBackgroundColor(Color.argb(100,51,153,255))
            }
            else -> {
                holder.view.lEventName?.setBackgroundColor(Color.WHITE)
            }

        }
        holder.event = event
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
            val EVENT_TIME = "8"
            val EVENT_TIMEZONE = "9"
            val EVENT_PRIVATE = "10"
            val EVENT_ISPUBLISHED = "11"
            val EVENT_ARCHIVED = "12"
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
                intent.putExtra(EVENT_TIME, event?.etime)
                intent.putExtra(EVENT_TIMEZONE, event?.etimezone)
                intent.putExtra(EVENT_PRIVATE, event?.eprivate)
                intent.putExtra(EVENT_ISPUBLISHED, event?.eispublished)
                intent.putExtra(EVENT_ARCHIVED, event?.eisarhived)

                view.context.startActivity(intent)
            }
        }
    }



}