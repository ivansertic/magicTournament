package com.ivansertic.magictournament.viewmodels

import android.util.Log
import android.widget.EditText
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.models.Tournament
import com.ivansertic.magictournament.repositories.TournamentRepository
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TournamentInfoViewModel : ViewModel() {

    var status: MutableLiveData<Any> = MutableLiveData()
    var dataCheck: Boolean = false
    private val tournamentRepository = TournamentRepository()
    val tournamentList = MutableLiveData<ArrayList<Tournament>>()

    fun checkData(title: TextInputLayout, description: TextInputLayout) {
        title.error = null
        description.error = null

        when {
            title.editText?.text.toString().isEmpty() -> {
                title.error = "Title cannot be empty!"
                title.requestFocus()
                dataCheck = false
                return
            }
            description.editText?.text.toString().isEmpty() -> {
                description.error = "Description cannot be empty"
                description.requestFocus()
                dataCheck = false
                return
            }
        }

        dataCheck = true
    }

    fun checkDate(date: TextInputLayout, time: TextInputLayout) {

        date.error = null
        time.error = null

        when {
            date.editText?.text.toString().isEmpty() -> {
                date.error = "Please select date!"
                date.requestFocus()
                return
            }
            time.editText?.text.toString().isEmpty() -> {
                time.error = "Please select time!"
                time.requestFocus()
                return
            }
        }

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")

        val inputDateTime: LocalDateTime = LocalDateTime.parse("${date.editText?.text} ${time.editText?.text}", formatter)

        val isDateValid = LocalDateTime.now().isBefore(inputDateTime)

        if (!isDateValid) {
            date.requestFocus()
            time.requestFocus()
            date.error = "Date or time is in the past!"
            time.error = "Date or time is in the past!"
        }

        status.value = isDateValid && dataCheck

    }

    fun fetchTournaments() {
        GlobalScope.launch(Dispatchers.IO) {
            val tournaments = tournamentRepository.getFirebaseTournaments()
            withContext(Main){
                tournamentList.value = tournaments
            }
        }
    }
}