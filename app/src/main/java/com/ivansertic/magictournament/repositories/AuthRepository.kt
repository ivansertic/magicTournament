package com.ivansertic.magictournament.repositories

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.ivansertic.magictournament.activities.Register
import com.ivansertic.magictournament.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.ivansertic.magictournament.models.enums.UserType
import com.ivansertic.magictournament.models.objects.UserObject
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.tasks.await
import java.lang.Error

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseFirestore.getInstance()
    val status = MutableLiveData<String>()

    fun register(email: String, username: String, password: String, userType: UserType) {

        GlobalScope.launch(Dispatchers.IO) {
            val exists: Boolean = checkUsername(username)

            if (exists) {
                withContext(Main) {
                    status.value = "Username already exists"
                }
                return@launch
            }

            val newUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = User(newUser.user!!.uid, email, username, password, userType)
            firebaseDatabase.collection("users").document(user.id).set(user)

            withContext(Main) {
                status.value = "Registration successful"
            }
        }
    }

    fun login(email: String, password: String) {

        GlobalScope.launch {
            try {
                loginUser(email, password)
                createUserObject()
                withContext(Main) {
                    status.value = "Successful"
                }
            } catch (e: FirebaseAuthException) {
                withContext(Main) {
                    status.value = "Incorrect e-mail or password!"
                }
            }
        }
    }

    private suspend fun checkUsername(username: String): Boolean {
        var users: MutableList<User> = mutableListOf()
        val collection = firebaseDatabase.collection("users").get().await()
        for (document in collection) {
            users.add(document.toObject(User::class.java))
        }

        if (users.any { user -> user.username == username }) {
            return true
        }

        return false
    }

    private suspend fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    private suspend fun createUserObject(){

        val currentUserId = firebaseAuth.currentUser?.uid ?: return

        val currentUserDocument = firebaseDatabase.collection("users").document(currentUserId).get().await()

        UserObject.createObject(currentUserDocument["type"] as String)

    }

}
