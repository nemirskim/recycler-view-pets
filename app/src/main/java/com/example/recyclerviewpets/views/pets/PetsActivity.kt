package com.example.recyclerviewpets.views.pets

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewpets.PetActionsListener
import com.example.recyclerviewpets.PetAdapter
import com.example.recyclerviewpets.R
import com.example.recyclerviewpets.databinding.PetsActivityBinding
import com.example.recyclerviewpets.models.Pet

class PetsActivity :
    AppCompatActivity(),
    PetsViewModelListener,
    PetActionsListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: PetsActivityBinding
    private lateinit var adapter: PetAdapter
    private lateinit var vm: PetsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PetsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PetAdapter(this)
        vm = PetsViewModel(this)

        val layoutManager = LinearLayoutManager(this)
        binding.rV.layoutManager = layoutManager
        binding.rV.adapter = adapter

        val spinnerAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                vm.getPetTypeSortCases()
            )
        binding.sortByPetTypeSpinner.adapter = spinnerAdapter
        binding.sortByPetTypeSpinner.onItemSelectedListener = this

        binding.favoritePetsButton.setOnClickListener {
            if (vm.toggleIsFavorite()) {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            } else {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
            }
        }

        binding.sortByNameButton.setOnClickListener {
            if (vm.toggleIsSortedByName()) {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            } else {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
            }
        }
    }

    // PetsViewModelListener
    override fun getPets(pets: List<Pet>) {
        if (pets.isNotEmpty()) {
            binding.noPetsTV.visibility = View.GONE
            binding.rV.visibility = View.VISIBLE
            adapter.pets = pets
        } else {
            binding.rV.visibility = View.GONE
            binding.noPetsTV.visibility = View.VISIBLE
        }
    }

    // PetActionsListener
    override fun onPetFavoriteStatus(pet: Pet) {
        vm.changeFavoriteStatus(pet)
    }

    override fun onPetRename(pet: Pet, name: String) {
        vm.renamePet(pet, name)
    }

    override fun onPetDelete(pet: Pet) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.delete_pet_title, pet.name))
            .setMessage(resources.getString(R.string.delete_pet_message, pet.name))
            .setPositiveButton(R.string.ok) { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    vm.deletePet(pet)
                }
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .create()
        dialog.show()
    }

    //  AdapterView.OnItemSelectedListener
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
        vm.changeSortType(position)

    override fun onNothingSelected(parent: AdapterView<*>?){}
}