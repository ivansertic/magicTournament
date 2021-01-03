package com.ivansertic.magictournament.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.ivansertic.magictournament.R

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val goToLoginButton = findViewById<MaterialButton>(R.id.goToLogin)

        goToLoginButton.setOnClickListener {
            this.finish()
        }
    }
}