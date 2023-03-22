package com.example.recyclerviewpets

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        if (it.isNotEmpty()) {
            adapter.pets = it
        } else {
            binding.rV.visibility = View.GONE
            binding.noPetsTV.visibility = View.VISIBLE
        }
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

    override fun onPetRename(pet: Pet, name: String) {
        petService.renamePet(pet, name)
    }

    override fun onPetDelete(pet: Pet) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.delete_pet_title, pet.name))
            .setMessage(resources.getString(R.string.delete_pet_message, pet.name))
            .setPositiveButton(R.string.ok) { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    petService.deletePet(pet)
                }
            }
            .setNegativeButton(R.string.no) {_, _ -> }
            .create()
        dialog.show()
    }
}