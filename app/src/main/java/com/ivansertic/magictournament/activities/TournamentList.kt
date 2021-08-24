package com.ivansertic.magictournament.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.adapters.RecViewAdapter
import com.ivansertic.magictournament.models.Tournament

class TournamentList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val tournamentList = ArrayList<Tournament>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_list)

        recyclerView = findViewById(R.id.recycleView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        for(i in 1..20){
            tournamentList.add(Tournament())
        }
        val adapter = RecViewAdapter(tournamentList)

        recyclerView.adapter = adapter
    }
}