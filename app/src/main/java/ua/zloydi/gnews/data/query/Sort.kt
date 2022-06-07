package ua.zloydi.gnews.data.query

import androidx.annotation.StringRes
import ua.zloydi.gnews.R

enum class Sort(val queryName: String, @StringRes val res: Int) {
	PUBLISHED_AT("publishedAt", R.string.published_at),
	RELEVANCE("relevance", R.string.relevance)
}