package ua.zloydi.gnews.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.zloydi.gnews.data.models.HistoryQuery
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS = "SearchHistoryDataSource"
private const val DELIMITER = Char.MIN_VALUE
private val KEY = stringPreferencesKey("SearchHistoryQueries")
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS)

@Singleton
class LocalSearchHistoryDataSource @Inject constructor(@ApplicationContext context: Context) :
	ReadWriteSearchHistoryDataSource {
	private val prefs = context.dataStore
	
	override suspend fun getQueries(): Flow<List<HistoryQuery>> = prefs.data.map { preferences ->
		preferences.getHistoryStringList().map { HistoryQuery(it) }
	}
	
	private fun Preferences.getHistoryStringList(): List<String> = this[KEY].run {
		this?.toHistoryQueryList() ?: emptyList()
	}
	
	private fun String.toHistoryQueryList() = split(DELIMITER)
	
	override suspend fun addQuery(query: HistoryQuery) {
		prefs.edit { preferences ->
			val list = preferences.getHistoryStringList().filter { it != query.query }
			preferences[KEY] = buildString {
				append(query.query)
				list.forEach {
					append(DELIMITER)
					append(it)
				}
			}
		}
	}
}