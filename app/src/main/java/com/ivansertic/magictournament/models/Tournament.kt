package com.ivansertic.magictournament.models

import com.google.firebase.firestore.PropertyName
import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat
import java.time.LocalDateTime
import java.util.*

data class Tournament(
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var type: TournamentFormat = TournamentFormat.CONSTRUCTED,
    var subType: Any = ConstructedTypes.MODERN,
    var tournamentDateTime: LocalDateTime = LocalDateTime.now(),
    var description: String = "",
    var tournamentLocation: TournamentLocation = TournamentLocation(),
    var creatorId: String = "",
    @JvmField @PropertyName("isFinished")
    var isFinished: Boolean = false
)
