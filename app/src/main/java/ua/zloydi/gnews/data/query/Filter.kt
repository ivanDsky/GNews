package ua.zloydi.gnews.data.query

import androidx.annotation.StringRes
import ua.zloydi.gnews.R
import java.util.*

enum class SearchIn(val queryName: String, @StringRes val res: Int) {
	TITLE("title", R.string.title),
	DESCRIPTION("description", R.string.description),
	CONTENT("content", R.string.content)
}

data class Filter(
	val from: Date? = null,
	val to: Date? = null,
	val searchIn: List<SearchIn> = emptyList(),
) : java.io.Serializable