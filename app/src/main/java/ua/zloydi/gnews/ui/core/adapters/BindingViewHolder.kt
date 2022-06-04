package ua.zloydi.gnews.ui.core.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BindingViewHolder<Item>(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
	abstract fun bind(item: Item)
}