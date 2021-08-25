package com.ivansertic.magictournament.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivansertic.magictournament.MapPopup
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.adapters.RecViewAdapter
import com.ivansertic.magictournament.models.Tournament

class TournamentList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val tournamentList = ArrayList<Tournament>()
    private lateinit var mapDialog: MapPopup

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

        mapDialog = MapPopup()

        adapter.setOnItemButtonClickListeners(object: RecViewAdapter.onItemButtonClickListener{
            override fun onApplyClick(position: Int) {
                Log.d("Apply Position", position.toString())
            }

            override fun onShowMapClick(position: Int) {
                Log.d("Map Position", position.toString())
                if(position < 10) {
                    mapDialog.lat = 0.0
                    mapDialog.long = 0.0
                }else{
                    mapDialog.lat = 45.554962158203125
                    mapDialog.long = 18.695512771606445
                }
                mapDialog.show(supportFragmentManager,"")
            }
        })
    }
}