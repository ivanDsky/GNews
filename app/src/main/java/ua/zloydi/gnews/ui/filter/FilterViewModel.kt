package ua.zloydi.gnews.ui.filter

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.zloydi.gnews.data.query.Filter
import ua.zloydi.gnews.data.query.SearchIn
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class FilterState(
	val from: String? = null, val to: String? = null, val searchIn: String? = null
)

@HiltViewModel
class FilterViewModel @Inject constructor(private val resources: Resources) : ViewModel() {
	private val _state = MutableStateFlow(FilterState())
	val state = _state.asStateFlow()
	private var result = Filter()
	private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
	
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
		if (isEmpty() || size == SearchIn.values().size) return null
		return buildString {
			forEach { it: SearchIn -> append("${resources.getString(it.res)}, ") }
			removeRange(length - 2, length)
		}
	}
	
}