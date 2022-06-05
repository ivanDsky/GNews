package ua.zloydi.gnews.ui.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.zloydi.gnews.data.models.ArticleListDTO
import ua.zloydi.gnews.data.repository.ArticlesRepository
import ua.zloydi.gnews.ui.core.models.ArticleUI
import ua.zloydi.gnews.ui.core.models.toUI
import javax.inject.Inject

sealed class State {
	object Loading : State()
	class Error(val message: String) : State()
	class ShowArticles(val articles: List<ArticleUI>) : State()
}

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
	private val repository: ArticlesRepository
) : ViewModel() {
	private val _state = MutableStateFlow<State>(State.Loading)
	val state = _state.asStateFlow()
	
	init {
		viewModelScope.launch {
			loadHeadlines()
		}
	}
	
	private suspend fun loadHeadlines() = withContext(Dispatchers.IO) {
		_state.value = State.Loading
		repository.getHeadlineArticles().onSuccess { articles: ArticleListDTO ->
			_state.value = State.ShowArticles(articles.toUI())
		}.onFailure { error: Throwable ->
			_state.value = State.Error(error.message ?: "Error")
		}
	}
	
	private fun ArticleListDTO.toUI(): List<ArticleUI> = articles.map {
		it.toUI { }
	}
}