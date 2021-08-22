package com.ivansertic.magictournament.models

data class TournamentLocation(
    var city: String = "",
    var country: String = "",
    var address: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0
)
