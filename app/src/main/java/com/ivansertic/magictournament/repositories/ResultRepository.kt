package com.ivansertic.magictournament.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.ivansertic.magictournament.models.TournamentRound

class ResultRepository {
    private val firebaseDatabase = FirebaseFirestore.getInstance()


    suspend fun updateTournamentRound(tournamentRound: TournamentRound){

        firebaseDatabase.collection("tournament_rounds").document(tournamentRound.id).set(tournamentRound)
    }
}