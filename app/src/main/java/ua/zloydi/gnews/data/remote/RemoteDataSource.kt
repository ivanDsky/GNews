package ua.zloydi.gnews.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import ua.zloydi.gnews.data.error.NetworkError
import ua.zloydi.gnews.data.models.ArticleListDTO
import ua.zloydi.gnews.data.query.Query
import ua.zloydi.gnews.data.query.toQueryMap
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
	private val remoteService: RetrofitService,
) : ReadArticlesDataSource {
	override suspend fun getArticles(query: Query): Result<ArticleListDTO> =
		withContext(Dispatchers.IO) {
			handleResponse { remoteService.getArticles(query.toQueryMap()) }
		}
	
	override suspend fun getHeadlineArticles(): Result<ArticleListDTO> =
		withContext(Dispatchers.IO) {
			handleResponse { remoteService.getHeadlineArticles(emptyMap()) }
		}
	
	private suspend fun <T> handleResponse(tryResponse: suspend () -> Response<T>) =
		withContext(Dispatchers.IO) {
			val response = tryResponse()
			try {
				when {
					response.isSuccessful.not() -> Result.failure(
						NetworkError.getErrorByCode(
							response.code(), response.errorBody()
						)
					)
					response.body() == null     -> Result.failure(IllegalStateException("Empty response"))
					else                        -> Result.success(response.body()!!)
				}
			} catch (networkException: IllegalStateException) {
				Result.failure(networkException)
			} catch (ioException: IOException) {
				Result.failure(ioException)
			} catch (exception: Exception) {
				Result.failure(exception)
			}
		}
}