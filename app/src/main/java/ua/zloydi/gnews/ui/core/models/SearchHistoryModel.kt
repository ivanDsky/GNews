package ua.zloydi.gnews.ui.core.models

import androidx.recyclerview.widget.DiffUtil
import ua.zloydi.gnews.data.models.HistoryQuery

data class SearchHistoryUI(
	val query: String,
	val onClick: () -> Unit = {},
)

class SearchHistoryUIDiff : DiffUtil.ItemCallback<SearchHistoryUI>() {
	override fun areItemsTheSame(oldItem: SearchHistoryUI, newItem: SearchHistoryUI) =
		oldItem.query == newItem.query
	
	override fun areContentsTheSame(oldItem: SearchHistoryUI, newItem: SearchHistoryUI) =
		oldItem.query == newItem.query
	
}

fun HistoryQuery.toUI(onClick: () -> Unit) = SearchHistoryUI(query = query, onClick = onClick)