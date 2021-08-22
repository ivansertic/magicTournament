package com.ivansertic.magictournament.models

import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat
import java.time.LocalDateTime

data class Tournament(
    var title: String = "",
    var type: TournamentFormat = TournamentFormat.CONSTRUCTED,
    var subType: Any = ConstructedTypes.MODERN,
    var tournamentDateTime: LocalDateTime = LocalDateTime.now(),
    var description: String = "",
    var tournamentLocation: TournamentLocation = TournamentLocation()
)
