package com.example.recyclerviewpets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recyclerviewpets.services.PetService

class MainActivity : AppCompatActivity() {
    private val petService: PetService
        get() = (applicationContext as App).petService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}