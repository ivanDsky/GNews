package ua.zloydi.gnews.ui.core

import ua.zloydi.gnews.ui.article.Article

sealed class OneShot {
	class OpenArticle(val article: Article) : OneShot()
	class ShowError(val message: String) : OneShot()
}