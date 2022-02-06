package com.mertoenjosh.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    private lateinit var toolbar_finish_activity: Toolbar
    private lateinit var btnFinish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        toolbar_finish_activity = findViewById(R.id.toolbar_finish_activity)
        setSupportActionBar(toolbar_finish_activity)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "FINISH"

        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btnFinish = findViewById(R.id.btnFinish)

        btnFinish.setOnClickListener {
            finish()
        }

        addDateToDatabase()

    }

    private fun addDateToDatabase() {
        val calender = Calendar.getInstance()
        val dateTime = calender.time

        Log.i("DATE", "$dateTime")

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        val dbHandler = SqliteOpenHelper(this, null)

        dbHandler.addDate(date)
        Log.i("DATE", "Added")
    }
}