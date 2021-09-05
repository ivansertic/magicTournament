package com.ivansertic.magictournament

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.ivansertic.magictournament.adapters.RecViewPopupAdapter
import com.ivansertic.magictournament.models.TournamentRound
import com.ivansertic.magictournament.viewmodels.ResultViewModel

class ResultsDialog: DialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecViewPopupAdapter
    lateinit var tournamentRound:TournamentRound
    private lateinit var resultViewModel: ResultViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView: View = inflater.inflate(R.layout.popup_results, container,false)

        recyclerView = mainView.findViewById(R.id.recycleViewPopup)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val tournamentRoundView: MaterialTextView = mainView.findViewById(R.id.round)
        tournamentRoundView.text = "Round: ${tournamentRound.roundNumber}"

        recyclerViewAdapter = RecViewPopupAdapter(tournamentRound.pairs)
        recyclerView.adapter = recyclerViewAdapter

        resultViewModel = ResultViewModel()

        sharedPreferences = activity?.getSharedPreferences("Owner", Context.MODE_PRIVATE)!!

        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelButton: MaterialButton = view.findViewById(R.id.resultCancel)
        val submitButton: MaterialButton = view.findViewById(R.id.resultSubmit)

        if(!sharedPreferences.getBoolean("isOwner", false) || sharedPreferences.getBoolean("isFinished", false)){
            submitButton.visibility = View.GONE
        }

        cancelButton.setOnClickListener {
            this.dismiss()
        }

        submitButton.setOnClickListener {
            updateScores()

            resultViewModel.updateTournamentRound(tournamentRound)
        }
    }

    private fun updateScores() {
        for(i in 1..tournamentRound.pairs.size){
            val view:View = recyclerView.getChildAt(i-1)

            val playerOne:TextInputLayout = view.findViewById(R.id.playerOne)
            val playerOneScore: TextInputLayout = view.findViewById(R.id.playerOneScore)
            val playerTwoScore: TextInputLayout = view.findViewById(R.id.playerTwoScore)


            for(pair in tournamentRound.pairs["pair${i}"]!!){
                if(pair.username == playerOne.editText?.text.toString()){
                    pair.score = playerOneScore.editText?.text.toString().toInt()
                }else{
                    pair.score = playerTwoScore.editText?.text.toString().toInt()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width,height)
    }
}