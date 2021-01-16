package com.ivansertic.magictournament

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.ivansertic.magictournament.activities.Register
import com.ivansertic.magictournament.activities.Tournament

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<MaterialButton>(R.id.goToRegister)
        button.setOnClickListener {
            val intentRegister = Intent(this,Tournament::class.java)
            startActivity(intentRegister)
        }
    }
}