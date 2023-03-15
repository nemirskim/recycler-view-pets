package com.example.recyclerviewpets

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerviewpets.databinding.ItemPetBinding
import com.example.recyclerviewpets.models.Pet

class PetAdapter : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {
    var pets = emptyList<Pet>()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPetBinding.inflate(inflater, parent, false)
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        val context = holder.itemView.context
        with(holder.binding) {
            holder.itemView.tag = pet
            petNameTV.text = pet.name
            petAgeTV.text =
                if (pet.age == 1) {
                    context.getString(R.string.a_year)
                } else {
                    context.getString(R.string.age, pet.age.toString())
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
            favoriteIV.tag = pet
            renameIV.tag = pet
            deleteIV.tag = pet
        }
    }

    override fun getItemCount(): Int = pets.size

    class PetViewHolder(val binding: ItemPetBinding) : RecyclerView.ViewHolder(binding.root)
}