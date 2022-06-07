package ua.zloydi.gnews.ui.filter

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.zloydi.gnews.data.query.Filter
import ua.zloydi.gnews.data.query.SearchIn
import java.text.SimpleDateFormat
import java.util.*

data class FilterState(
	val from: String? = null, val to: String? = null, val searchIn: String? = null
)

class FilterViewModel(private val resources: Resources, filter: Filter?) : ViewModel() {
	private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
	private var result = filter ?: Filter()
	private val _state = MutableStateFlow(
		FilterState(
			from = result.from?.let { dateFormatter.format(it) },
			to = result.to?.let { dateFormatter.format(it) },
			searchIn = result.searchIn.toUI()
		)
	)
	val state = _state.asStateFlow()
	
	fun setFromDate(date: Date) {
		_state.value = _state.value.copy(from = dateFormatter.format(date))
		result = result.copy(from = date)
	}
	
	fun setToDate(date: Date) {
		_state.value = _state.value.copy(to = dateFormatter.format(date))
		result = result.copy(to = date)
	}
	
	fun setSearchIn(list: List<SearchIn>) {
		_state.value = _state.value.copy(searchIn = list.toUI())
		result = result.copy(searchIn = list)
	}
	
	fun clearAll() {
		_state.value = FilterState()
		result = Filter()
	}
	
	fun getResult() = result
	
	private fun List<SearchIn>.toUI(): String? {
		if (isEmpty()) return null
		return buildString {
			forEach { it: SearchIn -> append("${resources.getString(it.res)}, ") }
			deleteRange(length - 2, length)
		}
	}
	
	class Factory(private val resources: Resources, private val filter: Filter?) :
		ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			if (modelClass == FilterViewModel::class.java) return FilterViewModel(
				resources, filter
			) as T
			throw IllegalStateException("Incorrect type")
		}
	}
}