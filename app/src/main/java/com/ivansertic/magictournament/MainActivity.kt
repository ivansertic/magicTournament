package com.ivansertic.magictournament

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.activities.TournamentLocation
import com.ivansertic.magictournament.activities.Register
import com.ivansertic.magictournament.activities.TournamentInfo
import com.ivansertic.magictournament.activities.TournamentList
import com.ivansertic.magictournament.viewmodels.LoginViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginViewModel: LoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setOnClickListeners(loginViewModel)

    }

    private fun setOnClickListeners(loginViewModel: LoginViewModel){
        val goToRegisterButton = findViewById<MaterialButton>(R.id.goToRegister)
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)

        goToRegisterButton.setOnClickListener {
            val intentRegister = Intent(this,Register::class.java)
            startActivity(intentRegister)
        }

        loginButton.setOnClickListener {
            this.loginUser(loginViewModel)
        }
    }

    private fun loginUser(loginViewModel: LoginViewModel) {
        val emailEditText: TextInputLayout = findViewById(R.id.emailLogin)
        val passwordEditText: TextInputLayout = findViewById(R.id.passwordLogin)

        loginViewModel.login(emailEditText, passwordEditText)
        loginViewModel.status.observe(this, { status ->
            status?.let{
                if(status == "Successful"){
                    loginViewModel.status.value = null
                    startActivity(Intent(this, TournamentList::class.java))
                }else {
                    loginViewModel.status.value = null
                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}