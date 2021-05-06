package com.ivansertic.magictournament.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.R
import com.ivansertic.magictournament.models.User
import com.ivansertic.magictournament.models.enums.UserType
import com.ivansertic.magictournament.viewmodels.RegisterViewModel

class Register : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var registerViewModel: RegisterViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        setOnClickListeners(registerViewModel)
    }

    private fun setOnClickListeners(registerViewModel: RegisterViewModel) {
        val goToLoginButton = findViewById<MaterialButton>(R.id.goToLogin)
        val register = findViewById<MaterialButton>(R.id.register)

        goToLoginButton.setOnClickListener {
            this.finish()
        }

        register.setOnClickListener {
            this.registerUser(registerViewModel)
        }
    }

    private fun registerUser(registerViewModel: RegisterViewModel) {
        val emailEditText : TextInputLayout = findViewById(R.id.emailRegister)
        val usernameEditText: TextInputLayout = findViewById(R.id.usernameRegister)
        val passwordEditText: TextInputLayout = findViewById(R.id.passwordRegister)
        val passwordConfirmationET: TextInputLayout = findViewById(R.id.passwordConfirmationRegister)
        val hostRadioButton: MaterialRadioButton = findViewById(R.id.accountHost)

        registerViewModel.checkData(emailEditText,usernameEditText,passwordEditText,passwordConfirmationET,hostRadioButton)
        registerViewModel.status.observe(this, Observer { status ->
            status?.let {
                registerViewModel.status.value = null
                Toast.makeText(this,status,Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}