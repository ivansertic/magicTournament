package com.ivansertic.magictournament.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ivansertic.magictournament.models.Tournament
import com.ivansertic.magictournament.models.TournamentLocation
import com.ivansertic.magictournament.models.UserTournamentFB
import com.ivansertic.magictournament.models.enums.ConstructedTypes
import com.ivansertic.magictournament.models.enums.LimitedTypes
import com.ivansertic.magictournament.models.enums.TournamentFormat
import com.ivansertic.magictournament.models.room.UserTournament
import com.ivansertic.magictournament.models.room.UserTournamentDao
import com.ivansertic.magictournament.utils.TournamentDocumentToObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TournamentRepository {

    var status: MutableLiveData<Boolean> = MutableLiveData()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseFirestore.getInstance()

    fun createTournament(
        country: TextInputLayout,
        city: TextInputLayout,
        address: TextInputLayout,
        sharedPreferences: SharedPreferences
    ) {
        val currentUser = firebaseAuth.currentUser ?: return

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
            tournamentLocation = tournamentLocation,
            creatorId = currentUser.uid,
            isFinished = false
        )

        GlobalScope.launch(Dispatchers.IO) {
            firebaseDatabase.collection("tournaments").document(tournament.id).set(tournament)
                .await()

            withContext(Main) {
                status.value = true
            }
        }


    }

    suspend fun getFirebaseTournaments(userTournamentDao: UserTournamentDao): ArrayList<Tournament> {
        val tournamentsList = ArrayList<Tournament>()

        val firebaseTournaments = firebaseDatabase.collection("tournaments").get().await()

        for (document in firebaseTournaments) {
            tournamentsList.add(TournamentDocumentToObject.convertDocumentToObject(document.data))
        }

        val currentUser = firebaseAuth.currentUser

        val tournamentIds = userTournamentDao.getAllAlreadyApplied(
            currentUser?.uid ?: ""
        )

        tournamentsList.removeAll{ tournament -> tournament.id in tournamentIds }
        return tournamentsList
    }

    suspend fun applyUserToTournament(
        userTournament: UserTournament,
        userTournamentDao: UserTournamentDao
    ) {

        val currentUser = firebaseAuth.currentUser ?: return
        userTournament.userId = currentUser.uid

        val check: UserTournament? = userTournamentDao.getAlreadyApplied(userTournament.userId, userTournament.tournamentId)

        if (check == null) {
            userTournamentDao.insertData(userTournament)
        }


        val userIds = ArrayList<String>()
        userIds.add(userTournament.userId)

        try {
            val userTournamentFB = firebaseDatabase.collection("tournament_users").whereEqualTo("tournamentId",userTournament.tournamentId).limit(1).get().await()

            var userTrnmObj =  UserTournamentFB(tournamentId = userTournament.tournamentId,userIds)
            if (userTournamentFB.documents.isNotEmpty()){
                var writeToFB = false
                for(document in userTournamentFB){
                    val userIds = document.data["userIds"] as ArrayList<String>
                    if(!userIds.contains(userTournament.userId)) {
                        userTrnmObj.userIds.addAll(document.data["userIds"] as ArrayList<String>)
                        writeToFB = true
                    }
                }

                if(writeToFB) {
                    firebaseDatabase.collection("tournament_users")
                        .document(userTournamentFB.documents[0].id).set(userTrnmObj).await()
                }
            }else{

                firebaseDatabase.collection("tournament_users").document().set(userTrnmObj).await()
            }
        }catch (e: FirebaseFirestoreException){
        }

    }

    suspend fun getMyTournaments(userTournamentDao: UserTournamentDao): ArrayList<Tournament> {
        val myTournamentsList = ArrayList<Tournament>()

        val firebaseTournaments = firebaseDatabase.collection("tournaments").get().await()

        for (document in firebaseTournaments) {
            myTournamentsList.add(TournamentDocumentToObject.convertDocumentToObject(document.data))
        }

        val currentUser = firebaseAuth.currentUser

        val tournamentIds = userTournamentDao.getAllAlreadyApplied(
            currentUser?.uid ?: ""
        )

        myTournamentsList.removeAll{tournament -> tournament.id !in tournamentIds }
        return myTournamentsList
    }

    suspend fun getMyCreatedTournaments(): ArrayList<Tournament> {
        val myTournamentsList = ArrayList<Tournament>()
        val currentUser = firebaseAuth.currentUser

        val firebaseTournaments = firebaseDatabase.collection("tournaments").whereEqualTo("creatorId",currentUser?.uid).get().await()

        for (document in firebaseTournaments) {
            myTournamentsList.add(TournamentDocumentToObject.convertDocumentToObject(document.data))
        }

        return myTournamentsList

    }

    fun checkOwner(tournament: Tournament): Boolean {
        return firebaseAuth.currentUser?.uid == tournament.creatorId
    }
}