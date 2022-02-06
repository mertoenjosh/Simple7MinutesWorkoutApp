package com.mertoenjosh.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    private lateinit var toolbar_history_activity: Toolbar
    private lateinit var tvNoDataAvailable: TextView
    private lateinit var rvHistory: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        toolbar_history_activity = findViewById(R.id.toolbar_history_activity)
        setSupportActionBar(toolbar_history_activity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "HISTORY"
        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // Initialize UI elements
        tvNoDataAvailable = findViewById(R.id.tvNoDataAvailable)
        rvHistory = findViewById(R.id.rvHistory)

        getAllCompletedDates()
    }

    private fun getAllCompletedDates() {
        val dbHandler = SqliteOpenHelper(this, null)

        val allCompletedDates = dbHandler.getAllCompleted()

        if (allCompletedDates.size > 0) {
            rvHistory.visibility = View.VISIBLE
            tvNoDataAvailable.visibility = View.GONE

            val historyAdapter = HistoryAdapter(this, allCompletedDates)
            rvHistory.layoutManager = LinearLayoutManager(this)
            rvHistory.adapter = historyAdapter
        } else {
            rvHistory.visibility = View.GONE
            tvNoDataAvailable.visibility = View.VISIBLE
        }
    }
}