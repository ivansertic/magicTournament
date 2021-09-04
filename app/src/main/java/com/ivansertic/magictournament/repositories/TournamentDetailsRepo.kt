package com.ivansertic.magictournament.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ivansertic.magictournament.models.TournamentRound
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.UserPair

import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TournamentDetailsRepo {

    private val firebaseDatabase = FirebaseFirestore.getInstance()

    suspend fun getContestants(tournamentId: String): ArrayList<User> {

        val tournamentUsersFB = firebaseDatabase.collection("tournament_users")
            .whereEqualTo("tournamentId", tournamentId).limit(1).get().await()

        val usersFB = firebaseDatabase.collection("users").get().await()

        val users = ArrayList<User>()
        for (document in usersFB) {
            users.add(document.toObject(User::class.java))
        }

        var contestantsIds: ArrayList<String> = ArrayList()

        for (document in tournamentUsersFB) {
            contestantsIds = document["userIds"] as ArrayList<String>
        }

        users.removeAll { item -> item.id !in contestantsIds }

        if (users.size % 2 != 0) {
            users.add(User(username = "Bye"))
        }

        return users
    }

    suspend fun addTournamentPairs(pairs: HashMap<String, ArrayList<UserPair>>, tournamentId: String,roundNumber:Int) {

      val newTournamentRound = TournamentRound(pairs = pairs,tournamentId = tournamentId,roundNumber = roundNumber)
        firebaseDatabase.collection("tournament_rounds").document(newTournamentRound.id).set(newTournamentRound).await()

    }

    suspend fun getRounds(tournamentId: String): ArrayList<TournamentRound>{

        val roundDocuments = firebaseDatabase.collection("tournament_rounds").whereEqualTo("tournamentId",tournamentId).get().await()

        val tournamentRounds: ArrayList<TournamentRound> = ArrayList()

        for(document in roundDocuments){
            val pairs = document["pairs"] as HashMap<*, *>
            val userPairs: HashMap<String,ArrayList<UserPair>> = HashMap()

            for(pair in pairs){

                val arrayContent = pair.value as ArrayList<HashMap<*,*>>
                val userPair: ArrayList<UserPair> = ArrayList()
                for(item in arrayContent){
                    userPair.add(UserPair(username = item["username"].toString(), score = item["score"].toString().toInt()))
                }
                userPairs[pair.key.toString()] = userPair
            }

            tournamentRounds.add(TournamentRound(
                id = document["id"].toString()
                ,tournamentId= document["tournamentId"].toString(),
                roundNumber = document["roundNumber"].toString().toInt(),
                pairs = userPairs
            ))
        }

        return tournamentRounds
    }
}