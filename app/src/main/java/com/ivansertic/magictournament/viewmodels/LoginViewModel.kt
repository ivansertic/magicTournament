package com.ivansertic.magictournament.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.repositories.AuthRepository
import java.util.regex.Pattern

class LoginViewModel: ViewModel() {

    var status = MutableLiveData<String>()
    private val authRepository: AuthRepository = AuthRepository()

    fun login(email: TextInputLayout, password: TextInputLayout){

        email.error = null
        password.error = null

        when {
            email.editText?.text.toString().isEmpty() -> {
                email.error = "Please enter email!"
                email.requestFocus()
                return
            }
            password.editText?.text.toString().isEmpty() -> {
                password.error = "Please enter password!"
                password.requestFocus()
                return
            }
        }

        val isEmailValid: Boolean = this.validateEmail(email)

        if(!isEmailValid){
            email.error = "Incorrect email format"
            email.requestFocus()
            return
        }
        authRepository.login(email.editText?.text.toString(), password.editText?.text.toString())
        authRepository.status.observeForever{status ->
            status?.let {
                authRepository.status.value = null
                this.status.value = status
            }
        }
    }

    private fun validateEmail(email: TextInputLayout): Boolean{
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        if(!EMAIL_ADDRESS_PATTERN.matcher(email.editText?.text.toString()).matches()){
            this.status.value = "Please enter valid email!"
            return false
        }

        return true
    }
}