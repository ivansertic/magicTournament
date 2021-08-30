package com.ivansertic.magictournament.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.UserPair
import com.ivansertic.magictournament.viewmodels.TournamentDetailsViewModel
import com.ivansertic.magictournament.viewmodels.TournamentInfoViewModel
import com.ivansertic.magictournament.viewmodels.TournamentLocationVM

class TournamentDetails : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var startButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var roundOneButton: MaterialButton
    private lateinit var roundTwoButton: MaterialButton
    private lateinit var roundThreeButton: MaterialButton
    private lateinit var finishButton: MaterialButton
    private lateinit var title: TextInputLayout
    private lateinit var type: TextInputLayout
    private lateinit var format: TextInputLayout
    private lateinit var tournamentDetailsVM: TournamentDetailsViewModel
    private lateinit var users: ArrayList<User>
    private var pairs: HashMap<String,ArrayList<UserPair>> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_details)

        startButton = findViewById(R.id.detailsStart)
        cancelButton = findViewById(R.id.detailsCancel)
        roundOneButton = findViewById(R.id.detailsRoundOne)
        roundTwoButton = findViewById(R.id.detailsRoundTwo)
        roundThreeButton = findViewById(R.id.detailsRoundThree)
        finishButton = findViewById(R.id.detailsFinish)
        title = findViewById(R.id.detailsTitle)
        type = findViewById(R.id.detailsType)
        format = findViewById(R.id.detailsFormat)

        sharedPreferences = getSharedPreferences("Owner", Context.MODE_PRIVATE)

        if(!sharedPreferences.getBoolean("isOwner",false)){
            startButton.visibility = View.GONE
        }

        tournamentDetailsVM = ViewModelProvider(this).get(TournamentDetailsViewModel::class.java)

        addData()
        getContestants()
        setOnClickListeners()

        tournamentDetailsVM.getRounds(sharedPreferences.getString("tournamentId","")!!)
    }

    private fun setOnClickListeners() {
        startButton.setOnClickListener {
            var roundNumber = when {
                roundOneButton.visibility != View.GONE -> {
                    2
                }
                roundTwoButton.visibility != View.GONE -> {
                    3
                }
                else -> {
                    1
                }
            }

            createPairs(users,roundNumber)
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun getContestants() {
        tournamentDetailsVM.getContestants(sharedPreferences.getString("tournamentId","")!!)
        tournamentDetailsVM.users.observe(this,{users -> users?.let {
            this.users = users
        }})
    }

    private fun createPairs(users: ArrayList<User>,roundNumber:Int) {
        pairs = tournamentDetailsVM.createPairs(users,sharedPreferences.getString("tournamentId","")!!,roundNumber)
    }

    private fun addData() {
        title.editText?.setText(sharedPreferences.getString("title",""))
        type.editText?.setText(sharedPreferences.getString("type",""))
        format.editText?.setText(sharedPreferences.getString("subType",""))
    }
}