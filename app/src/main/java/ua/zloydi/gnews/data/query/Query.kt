package ua.zloydi.gnews.data.query

data class Query(
	val q: String,
	val filter: Filter? = null,
	val sort: Sort = Sort.PUBLISHED_AT
)