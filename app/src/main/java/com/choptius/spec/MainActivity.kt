package com.choptius.spec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onNavigationClick(view: View) {
        when(view.id) {
            R.id.toDeepSkyButton -> startActivity(Intent(this, DeepSkyActivity::class.java))
        }
    }
}