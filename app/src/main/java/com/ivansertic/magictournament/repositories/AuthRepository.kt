package com.ivansertic.magictournament.repositories

import android.app.Application
import android.provider.ContactsContract
import android.widget.Toast
import com.ivansertic.magictournament.activities.Register
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.enums.UserType

class AuthRepository {

    fun register(email: String, username:String, password: String, userType: UserType,application: Register){
        Toast.makeText(application,"Tudeka sam",Toast.LENGTH_SHORT).show()
    }
}