package com.example.cromat.mathpath.activity.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.model.PetItem
import kotlinx.android.synthetic.main.sample_pet_item_view.view.*


class PetItemAdapter(val list: List<PetItem>) : RecyclerView.Adapter<PetItemAdapter.PetHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder =
            PetHolder(LayoutInflater.from(parent.context).inflate(R.layout.sample_pet_item_view, parent, false))

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.bind(list[position])
    }

    class PetHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(petItem: PetItem) {
            itemView.pet_item.petItem = petItem
        }
    }
}