package com.ivansertic.magictournament.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.R

class TournamentDetails : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var startButton: MaterialButton
    private lateinit var title: TextInputLayout
    private lateinit var type: TextInputLayout
    private lateinit var format: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_details)

        startButton = findViewById(R.id.detailsStart)
        title = findViewById(R.id.detailsTitle)
        type = findViewById(R.id.detailsType)
        format = findViewById(R.id.detailsFormat)

        sharedPreferences = getSharedPreferences("Owner", Context.MODE_PRIVATE)

        if(!sharedPreferences.getBoolean("isOwner",false)){
            startButton.visibility = View.GONE
        }

        addData()
    }

    private fun addData() {

    }
}