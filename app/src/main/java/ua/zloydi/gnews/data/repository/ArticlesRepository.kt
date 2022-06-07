package ua.zloydi.gnews.data.repository

import ua.zloydi.gnews.data.remote.ReadArticlesDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepository @Inject constructor(private val remoteDataSource: ReadArticlesDataSource) :
	ReadArticlesDataSource by remoteDataSource {}