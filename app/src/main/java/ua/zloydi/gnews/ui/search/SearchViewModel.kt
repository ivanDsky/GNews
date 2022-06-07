package ua.zloydi.gnews.ui.search

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.zloydi.gnews.R
import ua.zloydi.gnews.data.models.ArticleDTO
import ua.zloydi.gnews.data.models.HistoryQuery
import ua.zloydi.gnews.data.query.Filter
import ua.zloydi.gnews.data.query.Query
import ua.zloydi.gnews.data.query.Sort
import ua.zloydi.gnews.data.repository.ArticlesRepository
import ua.zloydi.gnews.data.repository.SearchHistoryRepository
import ua.zloydi.gnews.ui.article.Article
import ua.zloydi.gnews.ui.core.OneShot
import ua.zloydi.gnews.ui.core.models.ArticleUI
import ua.zloydi.gnews.ui.core.models.SearchHistoryUI
import ua.zloydi.gnews.ui.core.models.toUI
import javax.inject.Inject

sealed class State {
	class Empty : State()
	class ShowHistory(val history: List<SearchHistoryUI>) : State()
	class Loading : State()
	class ShowArticles(val totalArticles: String, val articles: List<ArticleUI>) : State()
}

data class SearchedState(
	val q: String = "", val filter: Int = 0, val isSortOpened: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchHistoryRepository: SearchHistoryRepository,
	private val articlesRepository: ArticlesRepository,
	private val resources: Resources
) : ViewModel() {
	private val _state = MutableStateFlow<State>(State.Empty())
	val state = _state.asStateFlow()
	
	private val _oneShot = Channel<OneShot>()
	val oneShot = _oneShot.receiveAsFlow()
	
	private val _queryState = MutableStateFlow(Query(""))
	private val _searchState = MutableStateFlow(SearchedState())
	val searchState = _searchState.asStateFlow()
	
	init {
		setHistoryScreen()
	}
	
	private suspend fun getHistory() = withContext(Dispatchers.IO) {
		searchHistoryRepository.getQueries().first()
	}
	
	@JvmName("toUIHistoryQuery")
	private fun List<HistoryQuery>.toUI() = map {
		it.toUI {
			updateQuery(it.query)
			search()
		}
	}
	
	fun isSearchEmpty() = _queryState.value.q.isBlank()
	
	fun setHistoryScreen() = viewModelScope.launch {
		updateQuery("")
		_state.value = State.Empty()
		val history = getHistory()
		if (_state.value is State.Empty) _state.value = State.ShowHistory(history.toUI())
	}
	
	fun search() = viewModelScope.launch {
		if (isSearchEmpty()) return@launch
		_state.value = State.Loading()
		addQuery(_queryState.value)
		val articlesResult = articlesRepository.getArticles(_queryState.value)
		if (articlesResult.isSuccess) {
			val articles = articlesResult.getOrThrow()
			_state.value = State.ShowArticles(
				resources.getString(R.string.x_news, articles.totalArticles),
				articles.articles.toUI()
			)
		} else {
			_oneShot.send(OneShot.ShowError(articlesResult.exceptionOrNull()?.message ?: "Error"))
		}
	}
	
	private suspend fun addQuery(query: Query) {
		searchHistoryRepository.addQuery(HistoryQuery(query.q))
	}
	
	fun updateQuery(query: String) {
		_queryState.value = _queryState.value.copy(q = query)
		_searchState.value = _searchState.value.copy(q = query)
	}
	
	fun updateFilter(filter: Filter?) {
		_queryState.value = _queryState.value.copy(filter = filter)
		_searchState.value = _searchState.value.copy(filter = filter.toInt())
	}
	
	private fun Filter?.toInt(): Int {
		if (this == null) return 0
		var ret = 0
		if (from != null) ret++
		if (to != null) ret++
		if (searchIn.isNotEmpty()) ret++
		return ret
	}
	
	fun updateSort(sort: Sort) {
		_queryState.value = _queryState.value.copy(sort = sort)
	}
	
	fun getFilter(): Filter? = _queryState.value.filter
	fun getSort(): Sort = _queryState.value.sort
	
	private fun List<ArticleDTO>.toUI() = map {
		it.toUI {
			viewModelScope.launch {
				if (it.url != null) _oneShot.send(
					OneShot.OpenArticle(Article(it.title ?: "", it.url))
				)
			}
		}
	}
	
}