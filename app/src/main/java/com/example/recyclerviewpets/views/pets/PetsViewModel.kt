package com.example.recyclerviewpets.views.pets

import com.example.recyclerviewpets.models.Pet
import com.example.recyclerviewpets.models.PetType
import com.example.recyclerviewpets.services.PetService
import com.example.recyclerviewpets.services.PetServiceListener

interface PetsViewModelListener {
    fun getPets(pets: List<Pet>)
}

class PetsViewModel(
    private val listener: PetsViewModelListener
) : PetServiceListener {
    internal val petTypes = PetType.values()
    private val petService = PetService()
    private var isFavorite = false
    private var isSortedByName = false
    private var petType = PetType.CAT

    init {
        petService.addListener(this)
    }

    fun toggleIsFavorite() : Boolean {
        isFavorite = !isFavorite
        petService.showAllPets()
        return isFavorite
    }

    fun toggleIsSortedByName(): Boolean {
        isSortedByName = !isSortedByName
        petService.showAllPets()
        return isSortedByName
    }

    fun changeSortType(type: PetType) {
        petType = type
        petService.showAllPets()
    }

    fun changeFavoriteStatus(pet: Pet) = petService.changeFavoriteStatus(pet)

    fun renamePet(pet: Pet, name: String) = petService.renamePet(pet, name)

    fun deletePet(pet: Pet) = petService.deletePet(pet)

    //  PetServiceListener
    override fun invoke(pets: List<Pet>) {
        var currentPets = pets
        if (isFavorite) {
            currentPets = currentPets.filter { it.isFavorite }
        }
        if (isSortedByName) {
            currentPets = currentPets.sortedBy { it.name }
        }

        currentPets = when (petType) {
            PetType.CAT -> currentPets.filter { it.type == PetType.CAT }
            PetType.DOG -> currentPets.filter { it.type == PetType.DOG }
            PetType.BIRD -> currentPets.filter { it.type == PetType.BIRD }
            PetType.PIG -> currentPets.filter { it.type == PetType.PIG }
        }
        listener.getPets(currentPets)
    }
}