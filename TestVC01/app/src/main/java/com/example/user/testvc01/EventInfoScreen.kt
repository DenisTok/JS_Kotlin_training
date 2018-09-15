package com.example.user.testvc01

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_event_info_screen.*

class EventInfoScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info_screen)

        val navBarTitle = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_NAME)
        supportActionBar?.title = navBarTitle

        tName.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_NAME)
        tWhere.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PLACE)
        tDate.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_DATE)
        tInfo.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_INFO)
        tPeople.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_PEOPLE)
        tPoints.text = intent.getStringExtra(MainAdapter.CustomViewHolder.EVENT_POINTS)
    }
}

