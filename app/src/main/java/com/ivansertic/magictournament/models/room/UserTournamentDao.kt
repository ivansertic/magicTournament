package com.ivansertic.magictournament.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface UserTournamentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertData(vararg userTournament: UserTournament)
}