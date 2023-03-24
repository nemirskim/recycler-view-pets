package com.example.recyclerviewpets

import android.app.Application
import com.example.recyclerviewpets.services.PetService

class App : Application() {
    val petService = PetService()
}