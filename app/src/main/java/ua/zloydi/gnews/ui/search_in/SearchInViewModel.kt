package ua.zloydi.gnews.ui.search_in

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.zloydi.gnews.data.query.SearchIn

data class SearchInState(
	val searchIn: SearchIn, val isEnabled: Boolean
) : java.io.Serializable

private const val ENABLED_DEFAULT = true

class SearchInViewModel : ViewModel() {
	private val _state: MutableStateFlow<List<SearchInState>>
	
	init {
		val initList = SearchIn.values().map { SearchInState(it, ENABLED_DEFAULT) }
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
		_state.value = _state.value.map { it.copy(isEnabled = ENABLED_DEFAULT) }
	}
	
	fun getResult(): List<SearchIn> = _state.value.filter { it.isEnabled }.map { it.searchIn }
}