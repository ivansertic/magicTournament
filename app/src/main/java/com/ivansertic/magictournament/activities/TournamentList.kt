package com.ivansertic.magictournament.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.ivansertic.magictournament.MapPopup
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.adapters.RecViewAdapter
import com.ivansertic.magictournament.models.Tournament
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.enums.UserType
import com.ivansertic.magictournament.models.objects.UserObject
import com.ivansertic.magictournament.models.room.UserTournament
import com.ivansertic.magictournament.viewmodels.TournamentInfoViewModel

class TournamentList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mapDialog: MapPopup
    private lateinit var tournamentViewModel: TournamentInfoViewModel
    private var tournamentList = ArrayList<Tournament>()
    private lateinit var adapter: RecViewAdapter
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tournament_list)
        bottomNav = findViewById(R.id.bottom_navigation)


        if(UserObject.getUserType() != UserType.HOST){
            bottomNav.menu.removeItem(R.id.navCreate)
            bottomNav.menu.removeItem(R.id.navCreated)
        }

        recyclerView = findViewById(R.id.recycleView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecViewAdapter(tournamentList)
        recyclerView.adapter = adapter

        tournamentViewModel =
            ViewModelProvider(this).get(TournamentInfoViewModel::class.java)


        mapDialog = MapPopup()

        fetchTournamentData()

        this.sharedPreferences = getSharedPreferences("Owner", Context.MODE_PRIVATE)

        adapter.setOnItemButtonClickListeners(object : RecViewAdapter.onItemButtonClickListener {
            override fun onApplyClick(position: Int) {
                val userTournament =
                    UserTournament(id = 0, tournamentId = adapter.getItem(position).id)
                tournamentViewModel.applyToTournament(userTournament)
                adapter.removeApplied(position)
            }

            override fun onShowMapClick(position: Int) {
                val tournament = adapter.getItem(position)
                mapDialog.lat = tournament.tournamentLocation.lat
                mapDialog.long = tournament.tournamentLocation.long
                mapDialog.show(supportFragmentManager, "")
            }

            override fun onDetailsClick(position: Int,context: Context) {
                val editor = sharedPreferences.edit()

                val tournament = adapter.getItem(position)
                editor.putBoolean("isOwner", tournamentViewModel.checkOwner(tournament))
                editor.putString("title",tournament.title)
                editor.putString("type",tournament.type.toString())
                editor.putString("subType",tournament.subType.toString())
                editor.putString("tournamentId",tournament.id)
                editor.putBoolean("isFinished",tournament.isFinished)
                editor.apply()

                startActivity(Intent(context,TournamentDetails::class.java))
            }
        })



        setBotNavClickListeners()
    }

    override fun onResume() {
        super.onResume()
        fetchTournamentData()
        bottomNav.selectedItemId = R.id.navAll
        adapter.hideShowButtons(true)
    }

    private fun setBotNavClickListeners() {
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navAll -> {
                    if(bottomNav.selectedItemId != R.id.navAll){
                        fetchTournamentData()
                        adapter.hideShowButtons(true)
                    }
                    true
                }
                R.id.navApplied -> {

                    if(bottomNav.selectedItemId != R.id.navApplied) {
                        fetchMyTournamentData()
                        adapter.hideShowButtons(false)
                    }

                    true
                }
                R.id.navCreate -> {
                    startActivity(Intent(this, TournamentInfo::class.java))
                    true
                }
                R.id.navCreated -> {
                    fetchMyCreatedTournaments()
                    adapter.hideShowButtons(false)
                    true
                }
                else -> {
                    bottomNav.selectedItemId = R.id.navAll
                    false
                }
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

    private fun fetchMyTournamentData(){
        tournamentViewModel.getMyTournaments()
        tournamentViewModel.tournamentList.observe(this,{tournamentList ->
            tournamentList?.let{
                adapter.changeData(tournamentList)
            }
        })
    }

    private fun fetchMyCreatedTournaments(){
        tournamentViewModel.getMyCreatedTournaments()
        tournamentViewModel.tournamentList.observe(this,{tournamentList ->
            tournamentList?.let{
                adapter.changeData(tournamentList)
            }
        })
    }
}