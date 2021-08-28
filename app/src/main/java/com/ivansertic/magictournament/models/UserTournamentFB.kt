package com.ivansertic.magictournament.models

data class UserTournamentFB(
    val tournamentId: String,
    val userIds: ArrayList<String>
)
