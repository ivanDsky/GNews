package ua.zloydi.gnews.ui.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.zloydi.gnews.databinding.LayoutSearchHistoryItemBinding
import ua.zloydi.gnews.ui.core.models.SearchHistoryUI
import ua.zloydi.gnews.ui.core.models.SearchHistoryUIDiff

class SearchHistoryAdapter :
	ListAdapter<SearchHistoryUI, SearchHistoryAdapter.ViewHolder>(SearchHistoryUIDiff()) {
	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return ViewHolder(LayoutSearchHistoryItemBinding.inflate(inflater, parent, false))
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItem(position)
		holder.bind(item)
	}
	
	class ViewHolder(private val binding: LayoutSearchHistoryItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: SearchHistoryUI) = with(binding) {
			root.text = item.query
			root.setOnClickListener { item.onClick() }
		}
	}
}