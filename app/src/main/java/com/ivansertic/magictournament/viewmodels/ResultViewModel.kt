package com.ivansertic.magictournament.viewmodels

import com.ivansertic.magictournament.models.TournamentRound
import com.ivansertic.magictournament.repositories.ResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultViewModel {
    private val resultRepository: ResultRepository = ResultRepository()

    fun updateTournamentRound(tournamentRound: TournamentRound){
        GlobalScope.launch(Dispatchers.IO){
            resultRepository.updateTournamentRound(tournamentRound)
        }
    }
}