package com.ivansertic.magictournament.viewmodels

import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.repositories.TournamentDetailsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TournamentDetailsViewModel: ViewModel() {

    var users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    private val detailsRepository = TournamentDetailsRepo()

    fun getContestants(tournamentId: String){
        GlobalScope.launch(Dispatchers.IO){
            val contestants = detailsRepository.getContestants(tournamentId)

            withContext(Main){
                users.value = contestants
            }
        }
    }
}