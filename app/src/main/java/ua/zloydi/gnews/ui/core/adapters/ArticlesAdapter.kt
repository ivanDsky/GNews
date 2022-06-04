package ua.zloydi.gnews.ui.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import ua.zloydi.gnews.databinding.LayoutArticlesItemBinding
import ua.zloydi.gnews.ui.core.models.ArticleUI
import ua.zloydi.gnews.ui.core.models.ArticleUIDiff

private const val HEADER_VIEW_TYPE = 0
private const val ARTICLE_VIEW_TYPE = 1

class ArticlesAdapter : ListAdapter<ArticleUI, BindingViewHolder<ArticleUI>>(ArticleUIDiff()) {
	
	override fun getItemViewType(position: Int): Int {
		return when (position) {
			0    -> HEADER_VIEW_TYPE
			else -> ARTICLE_VIEW_TYPE
		}
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): BindingViewHolder<ArticleUI> {
		val inflater = LayoutInflater.from(parent.context)
		return when (viewType) {
			HEADER_VIEW_TYPE  -> ArticleViewHolder(LayoutArticlesItemBinding.inflate(inflater))
			ARTICLE_VIEW_TYPE -> ArticleViewHolder(LayoutArticlesItemBinding.inflate(inflater))
			else              -> error("Undefined type")
		}
	}
	
	override fun onBindViewHolder(holder: BindingViewHolder<ArticleUI>, position: Int) {
		val item = getItem(position)
		holder.bind(item)
	}
	
	class ArticleViewHolder(private val binding: LayoutArticlesItemBinding) :
		BindingViewHolder<ArticleUI>(binding) {
		override fun bind(item: ArticleUI) = with(binding) {
			articleHeader.text = item.header
			articleBody.text = item.description
			Glide.with(root).load(item.image).into(articleIcon)
			root.setOnClickListener { item.onClick() }
		}
	}
}