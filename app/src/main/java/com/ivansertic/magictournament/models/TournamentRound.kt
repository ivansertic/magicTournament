package com.ivansertic.magictournament.models

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class TournamentRound(
    val id: String = UUID.randomUUID().toString(),
    val tournamentId: String,
    var pairs: HashMap<String,ArrayList<UserPair>>,
    val roundNumber: Int
)
