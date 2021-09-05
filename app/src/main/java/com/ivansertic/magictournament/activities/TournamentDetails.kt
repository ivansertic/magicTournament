package com.ivansertic.magictournament.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.ResultsDialog
import com.ivansertic.magictournament.models.TournamentRound
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.UserPair
import com.ivansertic.magictournament.viewmodels.TournamentDetailsViewModel
import com.ivansertic.magictournament.viewmodels.TournamentInfoViewModel
import com.ivansertic.magictournament.viewmodels.TournamentLocationVM
import kotlin.math.round

class TournamentDetails : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var winnerText: TextInputLayout
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
    private var users: ArrayList<User> = ArrayList()
    private lateinit var tournamentId: String
    private var rounds: ArrayList<TournamentRound> = ArrayList()
    private lateinit var resultDialog: ResultsDialog


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
        winnerText = findViewById(R.id.winner)

        sharedPreferences = getSharedPreferences("Owner", Context.MODE_PRIVATE)
        tournamentId = sharedPreferences.getString("tournamentId", "")!!

        if (!sharedPreferences.getBoolean("isOwner", false)) {
            startButton.visibility = View.GONE
        }

        tournamentDetailsVM = ViewModelProvider(this).get(TournamentDetailsViewModel::class.java)

        resultDialog = ResultsDialog()

        addData()
        getContestants()
        getRounds()
        setOnClickListeners()


    }

    private fun getRounds() {
        tournamentDetailsVM.getRounds(tournamentId)
        tournamentDetailsVM.rounds.observe(this, { rounds ->
            rounds?.let {
                this.rounds = rounds
                this.rounds.sortBy { round -> round.roundNumber }
                when (this.rounds.size) {
                    1 -> {
                        roundOneButton.visibility = View.VISIBLE
                    }
                    2 -> {
                        roundOneButton.visibility = View.VISIBLE
                        roundTwoButton.visibility = View.VISIBLE
                    }
                    3 -> {
                        roundOneButton.visibility = View.VISIBLE
                        roundTwoButton.visibility = View.VISIBLE
                        roundThreeButton.visibility = View.VISIBLE
                    }
                }

                checkForEnd()
            }
        })
    }

    private fun checkForEnd() {
        if(!sharedPreferences.getBoolean("isOwner", false)){
            if(sharedPreferences.getBoolean("isFinished",false)){
                declareWinner()
            }
            return
        }

        if(sharedPreferences.getBoolean("isFinished",false)){
            finishButton.visibility = View.GONE
            startButton.visibility = View.GONE
            declareWinner()
            return
        }

        if (users.size in 1..4  && roundOneButton.visibility != View.GONE) {
            startButton.visibility = View.GONE
            finishButton.visibility = View.VISIBLE
        } else if (users.size in 5..6 && roundTwoButton.visibility != View.GONE) {
            startButton.visibility = View.GONE
            finishButton.visibility = View.VISIBLE
        } else if (users.size > 6 && roundThreeButton.visibility != View.GONE) {
            startButton.visibility = View.GONE
            finishButton.visibility = View.VISIBLE
        }
    }

    private fun setOnClickListeners() {
        startButton.setOnClickListener {
            var roundNumber = when {
                (roundOneButton.visibility != View.GONE && roundTwoButton.visibility == View.GONE) -> {
                    2
                }
                roundTwoButton.visibility != View.GONE -> {
                    3
                }
                else -> {
                    1
                }
            }

            if (roundNumber == 1) {
                createPairs(users, roundNumber)
            } else {
                changePairs(roundNumber)
            }

        }

        cancelButton.setOnClickListener {
            finish()
        }

        roundOneButton.setOnClickListener {
            resultDialog.tournamentRound = rounds.find { round -> round.roundNumber == 1 }!!
            resultDialog.show(supportFragmentManager,"")
        }

        roundTwoButton.setOnClickListener {
            resultDialog.tournamentRound = rounds.find { round -> round.roundNumber == 2 }!!
            resultDialog.show(supportFragmentManager,"")
        }

        roundThreeButton.setOnClickListener {
            resultDialog.tournamentRound = rounds.find{ round -> round.roundNumber == 3}!!
            resultDialog.show(supportFragmentManager,"")
        }

        finishButton.setOnClickListener {
            val editor = sharedPreferences.edit()

            editor.putBoolean("isFinished",true)
            editor.apply()

            finishButton.visibility = View.GONE

            tournamentDetailsVM.finishTournament(tournamentId)
            declareWinner()
        }
    }

    private fun changePairs(roundNumber: Int) {
        tournamentDetailsVM.changePairs(
            rounds.find { round -> round.roundNumber == roundNumber - 1 }!!,
            roundNumber,
            tournamentId
        )

        this.getRounds()


    }

    private fun getContestants() {
        tournamentDetailsVM.getContestants(tournamentId)
        tournamentDetailsVM.users.observe(this, { users ->
            users?.let {
                this.users = users
                checkForEnd()
            }
        })
    }

    private fun createPairs(users: ArrayList<User>, roundNumber: Int) {
        if (users.size < 4) {
            Toast.makeText(this, "Too Few Contestants", Toast.LENGTH_LONG).show()
            return
        }
        tournamentDetailsVM.createPairs(users, tournamentId, roundNumber)
        this.getRounds()
    }

    private fun addData() {
        title.editText?.setText(sharedPreferences.getString("title", ""))
        type.editText?.setText(sharedPreferences.getString("type", ""))
        format.editText?.setText(sharedPreferences.getString("subType", ""))
    }

    private fun declareWinner(){
        winnerText.editText?.setText(tournamentDetailsVM.declareWinner(rounds,users))
        winnerText.visibility = View.VISIBLE
    }
}