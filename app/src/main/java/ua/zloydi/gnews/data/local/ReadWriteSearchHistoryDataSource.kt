package ua.zloydi.gnews.data.local

import kotlinx.coroutines.flow.Flow
import ua.zloydi.gnews.data.models.HistoryQuery

interface ReadWriteSearchHistoryDataSource {
	suspend fun getQueries() : Flow<List<HistoryQuery>>
	suspend fun addQuery(query: HistoryQuery)
}