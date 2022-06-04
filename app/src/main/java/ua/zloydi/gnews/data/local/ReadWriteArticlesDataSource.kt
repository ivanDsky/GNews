package ua.zloydi.gnews.data.local

import ua.zloydi.gnews.data.models.ArticleListDTO
import ua.zloydi.gnews.data.query.Query
import ua.zloydi.gnews.data.remote.ReadArticlesDataSource

interface ReadWriteArticlesDataSource : ReadArticlesDataSource {
	suspend fun cacheArticles(query: Query, response: ArticleListDTO)
	suspend fun cacheHeadlineArticles(response: ArticleListDTO)
}