package com.example.recyclerviewpets.views.pets

import com.example.recyclerviewpets.models.Pet
import com.example.recyclerviewpets.services.PetService
import com.example.recyclerviewpets.services.PetServiceListener

interface PetsViewModelListener {
    fun getPets(pets: List<Pet>)
}

class PetsViewModel(
    val listener: PetsViewModelListener
) : PetServiceListener {
    private val petService = PetService()
    private var isFavorite = false

    init {
        petService.addListener(this)
    }

    fun toggleIsFavorite() : Boolean {
        isFavorite = !isFavorite
        petService.showAllPets()
        return isFavorite
    }

    fun changeFavoriteStatus(pet: Pet) = petService.changeFavoriteStatus(pet)

    fun renamePet(pet: Pet, name: String) = petService.renamePet(pet, name)

    fun deletePet(pet: Pet) = petService.deletePet(pet)

    //    PetListener
    override fun invoke(pets: List<Pet>) {
        var currentPets = pets
        if (isFavorite) {
            currentPets = currentPets.filter { it.isFavorite }
        }
        listener.getPets(currentPets)
    }
}