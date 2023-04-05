package com.ziyad.fivefeat.main.user.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.fivefeat.databinding.ItemMenuBinding
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.utils.*
import java.util.*

class MenuAdapter(
    private val items: ArrayList<Menu>,
    private val onButtonClicked: (Menu, Int) -> Unit
) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    class MenuViewHolder(val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MenuViewHolder(ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = items[position]
        var latestCount = 0
        holder.binding.apply {
            ivMenuPhoto.loadImage(menu.photoUrl)
            tvMenuName.text = menu.name
            tvMenuPrice.text = menu.price.toString().toCurrencyFormat()
            tvMenuCount.text = latestCount.toString()
            btnPlus.setOnClickListener {
                latestCount++
                tvMenuCount.text = latestCount.toString()
                onButtonClicked(menu, latestCount)
            }
            btnMinus.setOnClickListener {
                if (latestCount > 0) {
                    latestCount--
                    tvMenuCount.text = latestCount.toString()
                    onButtonClicked(menu, latestCount)
                }
            }
        }
    }

    override fun getItemCount() = items.size
}