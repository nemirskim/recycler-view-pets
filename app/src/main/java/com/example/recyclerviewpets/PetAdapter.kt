package com.example.recyclerviewpets

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    View.OnClickListener,
    TextView.OnEditorActionListener {
    var pets = emptyList<Pet>()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPetBinding.inflate(inflater, parent, false)

        binding.petNameET.setOnEditorActionListener(this)
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
                petNameET.isEnabled = true
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

    // TextView.SetOnEditorActionListener
    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        val pet = v.tag as Pet
        return when (v.id) {
            R.id.petNameET -> {
                v.isEnabled = false
                actionsListener.onPetRename(pet, v.text.toString())
                true
            }
            else -> false
        }
    }

    class PetViewHolder(val binding: ItemPetBinding) : RecyclerView.ViewHolder(binding.root)
}