package com.ivansertic.magictournament.models.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_tournament")
data class UserTournament(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var userId: String = "",
    val tournamentId: String
)
