package com.cbu.mobil_dersi_projesi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cbu.mobil_dersi_projesi.databinding.ItemMekanBinding
import com.cbu.mobil_dersi_projesi.model.Mekan

class MekanRecyclerAdapter(
    private val mekanList: List<Mekan>,
    private val onItemClick: (mekan: Mekan) -> Unit,
    private val onDeleteClick: (mekan: Mekan) -> Unit
) :
    RecyclerView.Adapter<MekanViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MekanViewHolder(parent, onItemClick, onDeleteClick)

    override fun onBindViewHolder(holder: MekanViewHolder, position: Int) =
        holder.bind(mekanList[position])

    override fun getItemCount(): Int = mekanList.size
}


class MekanViewHolder(
    parent: ViewGroup,
    private val onItemClick: (mekan: Mekan) -> Unit,
    private val onDeleteClick: (mekan: Mekan) -> Unit,
    private val binding: ItemMekanBinding = ItemMekanBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(mekan: Mekan) {
        with(binding) {
            itemView.setOnClickListener { onItemClick(mekan) }
            name.text = mekan.name
            btnDelete.setOnClickListener { onDeleteClick(mekan) }
        }
    }

}