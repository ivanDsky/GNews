package ua.zloydi.gnews.data.query

import java.util.*

enum class SearchIn(val queryName: String){
	TITLE("title"), DESCRIPTION("description"), CONTENT("content")
}

data class Filter(
	val from: Date,
	val to: Date,
	val searchIn: List<SearchIn>,
)