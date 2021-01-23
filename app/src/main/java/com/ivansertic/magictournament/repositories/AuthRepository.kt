package com.ivansertic.magictournament.repositories

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ivansertic.magictournament.activities.Register
import com.ivansertic.magictournament.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ivansertic.magictournament.models.enums.UserType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    val status = MutableLiveData<String>()

    fun register(email: String, username: String, password: String, userType: UserType) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                val user = User(this.firebaseAuth.currentUser!!.uid,email,username,password,userType)
                firebaseDatabase.child("users").child(user.id).setValue(user)
                this.status.value = "Registration Successful"
            }else{
                this.status.value = "Opps! Something went wrong!"
            }
        }
    }

}