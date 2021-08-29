package com.ivansertic.magictournament.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserTournamentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertData(vararg userTournament: UserTournament)

    @Query("Select * FROM user_tournament WHERE userId = :userId AND tournamentId = :tournamentId GROUP BY tournamentId")
    fun getAlreadyApplied(userId: String, tournamentId: String): UserTournament?

    @Query("SELECT tournamentId FROM user_tournament WHERE userId = :userId GROUP BY tournamentId")
    fun getAllAlreadyApplied(userId: String): List<String>
}