package com.ivansertic.magictournament.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserTournament::class],version = 1, exportSchema = false)
abstract class UserTournamentDatabase: RoomDatabase() {

    abstract fun userTournamentDao(): UserTournamentDao

    companion object{
        @Volatile
        private var instance: UserTournamentDatabase? = null

        fun getDatabase(context: Context): UserTournamentDatabase{

            val tempInstance = instance

            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val  newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    UserTournamentDatabase::class.java,
                    "magic_database"
                ).build()

                instance = newInstance
                return newInstance
            }
        }
    }
}