package com.example.recyclerviewpets

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerviewpets.databinding.ItemPetBinding
import com.example.recyclerviewpets.models.Pet

interface PetActionsListener {
    fun onPetFavoriteStatus(pet: Pet)
    fun onPetRename(pet: Pet, name: String)
}

class PetAdapter(
    private val actionsListener: PetActionsListener
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>(),
    View.OnClickListener {
    var pets = emptyList<Pet>()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPetBinding.inflate(inflater, parent, false)

        binding.changeFavoriteStatusIV.setOnClickListener(this)

        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        val context = holder.itemView.context
        with(holder.binding) {
            holder.itemView.tag = pet
            petNameET.tag = pet
            changeFavoriteStatusIV.tag = pet
            renameIV.tag = pet
            deleteIV.tag = pet

            petNameET.setText(pet.name)

            if (pet.age == 1) {
                petAgeTV.text = context.getString(R.string.a_year)
            } else {
                petAgeTV.text = context.getString(R.string.age, pet.age.toString())
            }

            if (pet.photo.isNotBlank()) {
                Glide.with(petIV.context)
                    .load(pet.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_pet_icon)
                    .error(R.drawable.ic_pet_icon)
                    .into(petIV)
            } else {
                Glide.with(petIV.context)
                    .clear(petIV)
                petIV.setImageResource(R.drawable.ic_pet_icon)
            }

            if (pet.isFavorite) {
                changeFavoriteStatusIV.setImageResource(R.drawable.ic_star_fill)
            } else {
                changeFavoriteStatusIV.setImageResource(R.drawable.ic_star_border)
            }

            renameIV.setOnClickListener {
                petNameET.isEnabled = !petNameET.isEnabled
                if (petNameET.isEnabled) {
                    renameIV.setImageResource(R.drawable.ic_rename_pressed)
                } else {
                    renameIV.setImageResource(R.drawable.ic_rename_unpressed)
                }
            }

            petNameET.setOnEditorActionListener { v, actionId, event ->
                val renamedPet = v.tag as Pet
                when (v.id) {
                    R.id.petNameET -> {
                        v.isEnabled = false
                        renameIV.setImageResource(R.drawable.ic_rename_unpressed)
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            actionsListener.onPetRename(renamedPet, v.text.toString())
                            true
                        } else {
                            false
                        }
                    }
                    else -> false
                }
            }
        }
    }

    override fun getItemCount(): Int = pets.size

    // View.OnClickListener
    override fun onClick(v: View) {
        val pet = v.tag as Pet
        when (v.id) {
            R.id.changeFavoriteStatusIV ->
                actionsListener.onPetFavoriteStatus(pet)
        }
    }

    class PetViewHolder(val binding: ItemPetBinding) : RecyclerView.ViewHolder(binding.root)
}