package com.ivansertic.magictournament.viewmodels

import android.app.Application
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.activities.Register
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.enums.UserType
import com.ivansertic.magictournament.repositories.AuthRepository
import java.util.regex.Pattern

class RegisterViewModel: ViewModel()  {
    var status = MutableLiveData<String>()
    private  val authRepository: AuthRepository = AuthRepository()

    fun checkData(email: TextInputLayout, username:TextInputLayout, password: TextInputLayout, passwordConfirmation: TextInputLayout,userType: MaterialRadioButton){

        email.error = null
        username.error = null
        password.error = null
        passwordConfirmation.error = null

        when {
            email.editText?.text.toString().isEmpty() -> {
                email.error = "Please enter email!"
                email.requestFocus()
                return
            }
            username.editText?.text.toString().isEmpty() -> {
                username.error = "Please enter username!"
                username.requestFocus()
                return
            }
            password.editText?.text.toString().isEmpty() -> {
                password.error = "Please enter password!"
                password.requestFocus()
                return
            }
            passwordConfirmation.editText?.text.toString().isEmpty() -> {
                passwordConfirmation.error = "Please enter password confirmation!"
                passwordConfirmation.requestFocus()
                return
            }
        }

        val isEmailValid: Boolean = this.validateEmail(email)

        if(!isEmailValid){
            return
        }

        val isPasswordConfirmed: Boolean = this.checkPassword(password,passwordConfirmation)

        if(!isPasswordConfirmed){
            return
        }

        val type: UserType = if(userType.isChecked){
            UserType.HOST
        }else{
            UserType.PLAYER
        }

        authRepository.register(email.editText?.text.toString(),username.editText?.text.toString(),password.editText?.text.toString(), type)
        authRepository.status.observeForever {status ->
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

    private fun checkPassword(password: TextInputLayout, passwordConfirmation: TextInputLayout):Boolean{
        if(!password.editText?.text.toString().equals(passwordConfirmation.editText?.text.toString())){
            this.status.value = "Passwords do not match!"
            return false
        }
        return true
    }
}