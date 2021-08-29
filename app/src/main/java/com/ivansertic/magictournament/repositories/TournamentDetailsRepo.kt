package com.ivansertic.magictournament.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ivansertic.magictournament.models.User

import kotlinx.coroutines.tasks.await

class TournamentDetailsRepo {

    private val firebaseDatabase = FirebaseFirestore.getInstance()

    suspend fun getContestants(tournamentId:String): ArrayList<User> {

        val tournamentUsersFB = firebaseDatabase.collection("tournament_users")
            .whereEqualTo("tournamentId",tournamentId).limit(1).get().await()

        val usersFB = firebaseDatabase.collection("users").get().await()

        val users = ArrayList<User>()
        for(document in usersFB){
            users.add(document.toObject(User::class.java))
        }

        var contestantsIds: ArrayList<String> = ArrayList()

        for(document in tournamentUsersFB){
            contestantsIds = document["userIds"] as ArrayList<String>
        }

        users.removeAll{ item -> item.id  !in contestantsIds}

        return users
    }
}