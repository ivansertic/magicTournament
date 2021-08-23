package com.ivansertic.magictournament.utils

import com.ivansertic.magictournament.models.Tournament
import com.ivansertic.magictournament.models.TournamentLocation
import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.LimitedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TournamentDocumentToObject {

    fun convertDocumentToObject(data: Map<String, Any>?): Tournament {

        val tournamentLocationData: Map<String, Any> =
            data?.get("tournamentLocation") as Map<String, Any>
        val dateTimeData: Map<String, Any> = data["tournamentDateTime"] as Map<String, Any>

        val tournamentLocation: TournamentLocation = TournamentLocation(
            city = tournamentLocationData["city"].toString(),
            country = tournamentLocationData["country"].toString(),
            address = tournamentLocationData["address"].toString(),
            lat = tournamentLocationData["lat"].toString().toDouble(),
            long = tournamentLocationData["long"].toString().toDouble()
        )

        val tournamentType = TournamentFormat.valueOf(data["type"].toString())

        val tournamentSubType =
            if (tournamentType == TournamentFormat.CONSTRUCTED) ConstructedTypes.valueOf(data["subType"].toString()) else LimitedTypes.valueOf(
                data["subType"].toString()
            )

        val month = if(dateTimeData["monthValue"].toString().toInt() > 9) "${dateTimeData["monthValue"]}" else "0${dateTimeData["monthValue"]}"
        return Tournament(
            id = data["id"].toString(),
            title = data["title"].toString(),
            type = tournamentType,
            subType = tournamentSubType,
            tournamentDateTime = LocalDateTime.parse(
                "${dateTimeData["dayOfMonth"]}.${month}.${dateTimeData["year"]}. ${dateTimeData["hour"]}:${dateTimeData["minute"]}",
                DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")
            ),
            description = data["description"].toString(),
            tournamentLocation = tournamentLocation,
            creatorId = data["creatorId"].toString()
        )
    }
}