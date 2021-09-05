package com.ivansertic.magictournament.viewmodels

import android.util.Log
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivansertic.magictournament.models.TournamentRound
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.UserPair
import com.ivansertic.magictournament.repositories.TournamentDetailsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TournamentDetailsViewModel: ViewModel() {

    var users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    var rounds: MutableLiveData<ArrayList<TournamentRound>> = MutableLiveData()
    private val detailsRepository = TournamentDetailsRepo()

    fun getContestants(tournamentId: String){
        GlobalScope.launch(Dispatchers.IO){
            val contestants = detailsRepository.getContestants(tournamentId)

            withContext(Main){
                users.value = contestants
            }
        }
    }

    fun createPairs(users: ArrayList<User>, tournamentId: String,roundNumber:Int) {

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
    }

    fun getRounds(tournamentId: String) {
        GlobalScope.launch(Dispatchers.IO){
            val roundsFB = detailsRepository.getRounds(tournamentId)
            withContext(Main) {
                rounds.value = roundsFB
            }
        }
    }

    fun changePairs(tournamentRound: TournamentRound,roundNumber: Int,tournamentId: String){

        Log.d("Test", tournamentRound.toString())

        val userPairs: ArrayList<UserPair> = ArrayList()
        for(i in 1..tournamentRound.pairs.size){
            tournamentRound.pairs["pair${i}"]!![0].score = 0
            tournamentRound.pairs["pair${i}"]!![1].score = 0
            userPairs.add(tournamentRound.pairs["pair${i}"]!![1])
        }

        for(i in 1..tournamentRound.pairs.size){

            if( i == 1){
                tournamentRound.pairs["pair${i}"]!![1] = userPairs[tournamentRound.pairs.size - 1]
            }else{
                tournamentRound.pairs["pair${i}"]!![1] = userPairs[i-2]
            }

        }

        GlobalScope.launch(Dispatchers.IO){
            detailsRepository.addTournamentPairs(tournamentRound.pairs,tournamentId,roundNumber)
        }

    }

    fun finishTournament(tournamentId: String) {
        GlobalScope.launch(Dispatchers.IO){
            detailsRepository.finishTournament(tournamentId)
        }
    }

    fun declareWinner(rounds: ArrayList<TournamentRound>, users: ArrayList<User>): String {

        val finalResults: ArrayList<UserPair> = ArrayList()

        for(user in users){
            finalResults.add(UserPair(user.username))
        }

        for (round in rounds){
            for(pair in round.pairs.values){
                finalResults.find{user -> user.username ==  pair[0].username}?.score =
                    finalResults.find{ user -> user.username ==  pair[0].username}?.score?.plus(
                        pair[0].score
                    )!!

                finalResults.find{user -> user.username ==  pair[1].username}?.score =
                    finalResults.find{ user -> user.username ==  pair[1].username}?.score?.plus(
                        pair[1].score
                    )!!
            }
        }

        val winner = finalResults.maxWithOrNull(Comparator.comparingInt{it.score})

        if (winner != null) {
            return winner.username
        }

        return "Unknown"
    }
}