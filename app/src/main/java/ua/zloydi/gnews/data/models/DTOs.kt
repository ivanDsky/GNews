package ua.zloydi.gnews.data.models

data class ArticleListDTO(
	val totalArticles: Int = 0,
	val articles: List<ArticleDTO> = emptyList(),
)

data class ArticleDTO(
	val title: String? = null,
	val description: String? = null,
	val url: String? = null,
	val image: String? = null,
	val publishedAt: String? = null,
	val source: SourceDTO = SourceDTO()
)

data class SourceDTO(
	val name: String? = null,
	val url: String? = null,
)