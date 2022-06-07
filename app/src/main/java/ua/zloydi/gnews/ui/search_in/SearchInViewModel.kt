package ua.zloydi.gnews.ui.search_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.zloydi.gnews.data.query.SearchIn

data class SearchInState(
	val searchIn: SearchIn, val isEnabled: Boolean
) : java.io.Serializable

private val DEFAULT_SEARCH_IN = listOf(
	SearchInState(SearchIn.TITLE, true),
	SearchInState(SearchIn.DESCRIPTION, true),
	SearchInState(SearchIn.CONTENT, false)
)

class SearchInViewModel(startList: List<SearchIn>) : ViewModel() {
	private val _state: MutableStateFlow<List<SearchInState>>
	
	init {
		val initList = if (startList.isEmpty()) {
			DEFAULT_SEARCH_IN
		} else {
			SearchIn.values().map { SearchInState(it, startList.contains(it)) }
		}
		_state = MutableStateFlow(initList)
	}
	
	val state = _state.asStateFlow()
	
	fun update(searchIn: SearchIn, isEnabled: Boolean) {
		_state.value = _state.value.mapIndexed { index, searchInState ->
			if (index == searchIn.ordinal) SearchInState(searchIn, isEnabled)
			else searchInState
		}
	}
	
	fun clearAll() {
		_state.value = DEFAULT_SEARCH_IN
	}
	
	fun getResult() = _state.value.filter { it.isEnabled }.map { it.searchIn }
	
	class Factory(private val startList: List<SearchIn>) : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			if (modelClass == SearchInViewModel::class.java) return SearchInViewModel(startList) as T
			throw IllegalStateException("Incorrect type")
		}
	}
	
}