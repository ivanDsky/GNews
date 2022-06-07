package ua.zloydi.gnews.ui.sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.zloydi.gnews.data.query.Sort

data class SortState(val sort: Sort, val isInit: Boolean)

class SortViewModel(sort: Sort) : ViewModel() {
	private val _state = MutableStateFlow(SortState(sort, true))
	val state = _state.asStateFlow()
	
	fun setSort(sort: Sort) {
		_state.value = SortState(sort, false)
	}
	
	class Factory(private val sort: Sort) : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			if (modelClass == SortViewModel::class.java) return SortViewModel(sort) as T
			throw IllegalStateException("Incorrect type")
		}
	}
}