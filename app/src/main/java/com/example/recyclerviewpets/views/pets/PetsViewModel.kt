package com.example.recyclerviewpets.views.pets

import com.example.recyclerviewpets.models.ListState
import com.example.recyclerviewpets.models.Pet
import com.example.recyclerviewpets.models.PetType
import com.example.recyclerviewpets.services.PetService
import com.example.recyclerviewpets.services.PetServiceListener

interface PetsViewModelListener {
    fun getPets(pets: List<Pet>)
    fun getState(state: ListState)
    fun countPets(amount: Int)
}

class PetsViewModel(
    private val listener: PetsViewModelListener
) : PetServiceListener {
    private val petService = PetService()
    private var isFavorite = false
    private var isSortedByName = false
    private var petType: PetType? = null

    init {
        petService.addListener(this)
    }

    fun getPetTypeSortCases() =
        PetType.values()
            .map { it.raw }
            .toMutableList()
            .apply {
                this.add(0, "All")
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

    fun changeSortType(position: Int) {
        petType = if (position == 0) {
            null
        } else {
            PetType.values()[position - 1]
        }
        petService.showAllPets()
    }

    fun changeFavoriteStatus(pet: Pet) = petService.changeFavoriteStatus(pet)

    fun renamePet(pet: Pet, name: String) = petService.renamePet(pet, name)

    fun deletePet(pet: Pet) = petService.deletePet(pet)

    //  PetServiceListener
    override fun invoke(pets: List<Pet>) {
        val currentState: ListState
        var currentPets = pets
        val amount = pets.size
        if (pets.isEmpty()) {
            currentState = ListState.EMPTY
            petType = null
        } else {
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
                null -> currentPets
            }

            currentState = if (currentPets.isEmpty() && (isFavorite || petType != null)) {
                ListState.SORT
            } else {
                ListState.FULL
            }
        }

        listener.getPets(currentPets)
        listener.getState(currentState)
        listener.countPets(amount)
    }
}