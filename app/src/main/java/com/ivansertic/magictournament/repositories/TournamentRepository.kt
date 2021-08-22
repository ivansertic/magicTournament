package com.ivansertic.magictournament.repositories

import android.content.SharedPreferences
import android.location.Address
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.models.Tournament
import com.ivansertic.magictournament.models.TournamentLocation
import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.LimitedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TournamentRepository {

    private var status: MutableLiveData<Boolean> = MutableLiveData()

    fun createTournament(
        country: TextInputLayout,
        city: TextInputLayout,
        address: TextInputLayout,
        sharedPreferences: SharedPreferences
    ) {
        val tournamentLocation: TournamentLocation = TournamentLocation(
            city = city.editText?.text.toString(),
            address = address.editText?.text.toString(),
            country = country.editText?.text.toString(),
            lat = sharedPreferences.getFloat("lat", 0.0F).toDouble(),
            long = sharedPreferences.getFloat("long", 0.0F).toDouble()
        )



        val tournament: Tournament = Tournament(
            title = sharedPreferences.getString("tournamentTitle", "default").toString(),
            type = TournamentFormat.valueOf(
                sharedPreferences.getString("tournamentType", "default").toString()
            ),
            subType = if (TournamentFormat.valueOf(
                    sharedPreferences.getString(
                        "tournamentType",
                        "default"
                    ).toString()
                ) == TournamentFormat.CONSTRUCTED
            )
                ConstructedTypes.valueOf(
                    sharedPreferences.getString("tournamentSubType", "default").toString()
                )
            else LimitedTypes.valueOf(
                sharedPreferences.getString("tournamentSubType", "default").toString()
            ),
            tournamentDateTime = LocalDateTime.parse(
                sharedPreferences.getString("tournamentDate", "default").toString()
                        + " " +
                        sharedPreferences.getString("tournamentTime", "default").toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")
            ),
            description = sharedPreferences.getString("tournamentDescription", "default")
                .toString(),
            tournamentLocation = tournamentLocation

        )

    }
}