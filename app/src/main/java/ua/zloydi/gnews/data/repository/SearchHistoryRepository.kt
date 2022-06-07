package ua.zloydi.gnews.data.repository

import ua.zloydi.gnews.data.local.ReadWriteSearchHistoryDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryRepository @Inject constructor(localDataSource: ReadWriteSearchHistoryDataSource) :
	ReadWriteSearchHistoryDataSource by localDataSource