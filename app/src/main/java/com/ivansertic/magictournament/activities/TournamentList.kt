package com.ivansertic.magictournament.activities

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ivansertic.magictournament.MapPopup
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.adapters.RecViewAdapter
import com.ivansertic.magictournament.models.Tournament
import com.ivansertic.magictournament.models.room.UserTournament
import com.ivansertic.magictournament.viewmodels.TournamentInfoViewModel

class TournamentList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mapDialog: MapPopup
    private lateinit var tournamentViewModel: TournamentInfoViewModel
    private var tournamentList = ArrayList<Tournament>()
    private lateinit var adapter: RecViewAdapter
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tournament_list)
        bottomNav = findViewById(R.id.bottom_navigation)


        recyclerView = findViewById(R.id.recycleView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecViewAdapter(tournamentList)
        recyclerView.adapter = adapter

        tournamentViewModel =
            ViewModelProvider(this).get(TournamentInfoViewModel::class.java)


        mapDialog = MapPopup()

        fetchTournamentData()

        adapter.setOnItemButtonClickListeners(object : RecViewAdapter.onItemButtonClickListener {
            override fun onApplyClick(position: Int) {
                val userTournament =
                    UserTournament(id = 0, tournamentId = adapter.getItem(position).id)
                tournamentViewModel.applyToTournament(userTournament)
            }

            override fun onShowMapClick(position: Int) {
                val tournament = adapter.getItem(position)
                mapDialog.lat = tournament.tournamentLocation.lat
                mapDialog.long = tournament.tournamentLocation.long
                mapDialog.show(supportFragmentManager, "")
            }
        })

        setBotNavClickListeners()
    }

    override fun onResume() {
        super.onResume()

        fetchTournamentData()
    }

    private fun setBotNavClickListeners() {
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navAll -> {
                    true
                }
                R.id.navApplied -> {
                    true
                }
                R.id.navCreate -> {
                    startActivity(Intent(this, TournamentInfo::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchTournamentData() {
        this.tournamentViewModel.fetchTournaments()
        tournamentViewModel.tournamentList.observe(this, { tournamentList ->
            tournamentList?.let {
                adapter.changeData(tournamentList)
            }
        })
    }
}