package ua.zloydi.gnews.data.remote

import ua.zloydi.gnews.data.models.ArticleListDTO
import ua.zloydi.gnews.data.query.Query

interface ReadArticlesDataSource {
	suspend fun getArticles(query: Query) : Result<ArticleListDTO>
	suspend fun getHeadlineArticles() : Result<ArticleListDTO>
}