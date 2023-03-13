package com.example.recyclerviewpets.services

import com.example.recyclerviewpets.models.Pet
import com.github.javafaker.Faker
import kotlin.random.Random

typealias PetListener = (pets: List<Pet>) -> Unit

class PetService {
    private var pets = mutableListOf<Pet>()
    private var listeners = mutableSetOf<PetListener>()

    init {
        val faker = Faker.instance()
        pets = (0..9).map {
            Pet(
                Random.nextLong(1_000_000_000, 9_999_999_999),
                faker.animal().name(),
                Random.nextInt(1, 18),
                PHOTOS[it]
            )
        }.toMutableList()
    }

    fun renamePet(pet: Pet, name: String) {
        val index = getIndex(pet)
        if (index != -1) {
            val nPet = pet.copy(name = name)
            pets[index] = nPet
            notifyChanges()
        }
    }

    fun changeFavoriteStatus(pet: Pet) {
        val index = getIndex(pet)
        if (index != -1) {
            if (pet.isFavorite) {
                val nPet = pet.copy(isFavorite = false)
                pets[index] = nPet
                notifyChanges()
            } else {
                val nPet = pet.copy(isFavorite = true)
                pets[index] = nPet
                notifyChanges()
            }
        }
    }

    fun deletePet(pet: Pet) {
        val index = getIndex(pet)
        if (index != -1) {
            pets.removeAt(index)
            notifyChanges()
        }
    }

    fun addListener(listener: PetListener) {
        listeners.add(listener)
        listener.invoke(pets)
    }

    fun removeListener(listener: PetListener) = listeners.remove(listener)

    private fun getPet(id: Long): Pet = pets.first { it.id == id }

    private fun getIndex(pet: Pet): Int = pets.indexOfFirst { it.id == pet.id }

    private fun notifyChanges() = listeners.forEach { it.invoke(pets) }

    companion object {
        private val PHOTOS = mutableListOf(
            "https://images.unsplash.com/photo-1523626797181-8c5ae80d40c2?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8YW5pbWFsfHx8fHx8MTY3NzYxOTgwMg&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1506891536236-3e07892564b7?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8JGFuaW1hbHx8fHx8fDE2Nzc2MTk5MDg&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1552728089-57bdde30beb3?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8YW5pbWFsfHx8fHx8MTY3NzYxOTkzOQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1535930749574-1399327ce78f?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8YW5pbWFsfHx8fHx8MTY3NzYyMDAyMA&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1472053217156-31b42df2319c?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY3NzYyMDA1Nw&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1558788353-f76d92427f16?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8ZG9nfHx8fHx8MTY3NzYyMDA3OA&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1560813562-fd09315f5ce8?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8cGFycm90fHx8fHx8MTY3NzYyMDExMQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1631558996580-739c9b38db91?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8cGlnfHx8fHx8MTY3NzYyMDE1MA&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1562119464-7480f81577cc?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY3NzYyMDE4MQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1583511666445-775f1f2116f5?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218MHx8ZG9nfHx8fHx8MTY3NzYyMDIxOQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
        )
    }
}