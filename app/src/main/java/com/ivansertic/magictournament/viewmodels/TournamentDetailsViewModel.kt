package com.ivansertic.magictournament.viewmodels

import android.util.Log
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.UserPair
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

    fun createPairs(users: ArrayList<User>, tournamentId: String,roundNumber:Int): HashMap<String,ArrayList<UserPair>> {

        users.shuffle()
        val pairs: HashMap<String,ArrayList<UserPair>> = HashMap()

        var pairNumber = 1
        for(i in 0 until users.size - 1 step 2 ){
            val userPairs: ArrayList<UserPair> = ArrayList()
            userPairs.add(UserPair(username = users[i].username))
            userPairs.add(UserPair(username = users[i+1].username))

            pairs["pair${pairNumber}"] = userPairs

            pairNumber++
        }

        GlobalScope.launch(Dispatchers.IO){
            detailsRepository.addTournamentPairs(pairs,tournamentId,roundNumber)
        }

        return pairs
    }

    fun getRounds(tournamentId: String) {
        GlobalScope.launch(Dispatchers.IO){
            detailsRepository.getRounds(tournamentId)
        }
    }
}