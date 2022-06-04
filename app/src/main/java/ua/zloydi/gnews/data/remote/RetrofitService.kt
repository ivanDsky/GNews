package ua.zloydi.gnews.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ua.zloydi.gnews.BuildConfig
import ua.zloydi.gnews.data.models.ArticleListDTO



interface RetrofitService {
	companion object{
		const val LINK = "https://gnews.io/api/v4/"
		private const val HEADLINES = "top-headlines"
		private const val SEARCH = "search"
	}
	@GET(SEARCH)
	suspend fun getArticles(
		@QueryMap queryMap: Map<String, String>,
		@Query("token") token: String
	): Response<ArticleListDTO>
	
	@GET(HEADLINES)
	suspend fun getHeadlineArticles(
		@QueryMap queryMap: Map<String, String>,
		@Query("token") token: String
	): Response<ArticleListDTO>
}

suspend fun RetrofitService.getArticles(
	queryMap: Map<String, String>
) = getArticles(queryMap, BuildConfig.API_KEY)

suspend fun RetrofitService.getHeadlineArticles(
	queryMap: Map<String, String>
) = getHeadlineArticles(queryMap, BuildConfig.API_KEY)