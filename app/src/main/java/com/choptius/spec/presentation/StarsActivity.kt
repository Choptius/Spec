package com.choptius.spec.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.choptius.spec.databinding.ActivityStarsBinding

class StarsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStarsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val spinnerAdapter =
//            ArrayAdapter.createFromResource(this, R.array.catalogs, R.layout.spinner_item)
//        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item)
//        binding.spinner.adapter = spinnerAdapter


    }
}