package com.ziyad.fivefeat.main.restaurant.ui.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.fivefeat.databinding.ItemMenuRestaurantBinding
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.utils.loadImage
import com.ziyad.fivefeat.utils.toCurrencyFormat

class MenuRestaurantAdapter(
    private val items: ArrayList<Menu>,
    private val onButtonClicked: (Menu) -> Unit
) :
    RecyclerView.Adapter<MenuRestaurantAdapter.RestaurantMenuViewHolder>() {
    class RestaurantMenuViewHolder(val binding: ItemMenuRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RestaurantMenuViewHolder(
            ItemMenuRestaurantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        val menu = items[position]
        holder.apply {
            binding.apply {
                ivMenuPhoto.loadImage(menu.photoUrl)
                tvMenuName.text = menu.name
                tvMenuPrice.text = menu.price.toString().toCurrencyFormat()
                btnDelete.setOnClickListener {
                    onButtonClicked(menu)
                }
            }
        }
    }
}