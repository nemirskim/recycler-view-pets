package com.example.recyclerviewpets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewpets.databinding.ActivityMainBinding
import com.example.recyclerviewpets.models.Pet
import com.example.recyclerviewpets.services.PetListener
import com.example.recyclerviewpets.services.PetService

class MainActivity : AppCompatActivity(), PetActionsListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PetAdapter
    private val petService: PetService
        get() = (applicationContext as App).petService
    private val petListener: PetListener = {
        adapter.pets = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PetAdapter(this)

        val layoutManager = LinearLayoutManager(this)
        binding.rV.layoutManager = layoutManager
        binding.rV.adapter = adapter

        petService.addListener(petListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        petService.removeListener(petListener)
    }

//    PetActionsListener
    override fun onPetFavoriteStatus(pet: Pet) {
        petService.changeFavoriteStatus(pet)
    }
}