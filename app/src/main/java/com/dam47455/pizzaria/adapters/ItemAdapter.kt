package com.dam47455.pizzaria.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dam47455.pizzaria.MainActivity
import com.dam47455.pizzaria.databinding.ItemOnBoardBinding
import com.dam47455.pizzaria.fragments.SearchFragment
import com.dam47455.pizzaria.widgets.Item

class ItemAdapter(val itemList: List<Item>, private var main: MainActivity): RecyclerView.Adapter<ItemAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(var binding: ItemOnBoardBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemOnBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.apply {
            catImage.setImageResource(item.image)
            catText.text = item.text
            catItem.setBackgroundColor(ContextCompat.getColor(root.context, item.background))
            root.setOnClickListener{
                main.selectCategory(item.text)
                main.changeFragment(SearchFragment(main))
            }
        }
    }

    override fun getItemCount() = itemList.size
}