package com.ivansertic.magictournament.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.repositories.TournamentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TournamentLocationVM : ViewModel() {

    private val tournamentRepository: TournamentRepository = TournamentRepository()
    var status: MutableLiveData<Boolean> = MutableLiveData()


    fun checkData(
        country: TextInputLayout,
        city: TextInputLayout,
        address: TextInputLayout,
        sharedPreferences: SharedPreferences
    ) {

        country.error = null
        city.error = null
        address.error = null

        when {
            country.editText?.text.toString().isEmpty() -> {
                country.error = "Country cannot be empty!"
                country.requestFocus()
                return
            }
            city.editText?.text.toString().isEmpty() -> {
                city.error = "City cannot be empty!"
                city.requestFocus()
                return
            }
            address.editText?.text.toString().isEmpty() -> {
                address.error = "Address cannot be empty!"
                address.requestFocus()
                return
            }
        }

        tournamentRepository.createTournament(country, city, address, sharedPreferences)

        tournamentRepository.status.observeForever { status ->
            status?.let {
                if (status) {
                    tournamentRepository.status.value = null
                    this.status.value = true
                }
            }
        }


    }
}