package ua.zloydi.gnews.ui.core.models

import androidx.recyclerview.widget.DiffUtil
import ua.zloydi.gnews.data.models.ArticleDTO

data class ArticleUI(
	val image: String? = null,
	val header: String? = null,
	val description: String? = null,
	val onClick: () -> Unit = {},
)

class ArticleUIDiff : DiffUtil.ItemCallback<ArticleUI>() {
	override fun areItemsTheSame(oldItem: ArticleUI, newItem: ArticleUI) =
		oldItem.header == newItem.header && oldItem.description == newItem.description && oldItem.image == newItem.image
	
	override fun areContentsTheSame(oldItem: ArticleUI, newItem: ArticleUI) =
		oldItem.header == newItem.header && oldItem.description == newItem.description && oldItem.image == newItem.image
	
}

fun ArticleDTO.toUI(onClick: () -> Unit) = ArticleUI(
	image = image, header = title, description = description, onClick = onClick
)