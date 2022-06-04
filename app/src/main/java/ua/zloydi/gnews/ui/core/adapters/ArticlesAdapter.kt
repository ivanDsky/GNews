package ua.zloydi.gnews.ui.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.zloydi.gnews.databinding.LayoutArticlesItemBinding
import ua.zloydi.gnews.ui.core.models.ArticleUI
import ua.zloydi.gnews.ui.core.models.ArticleUIDiff

class ArticlesAdapter : ListAdapter<ArticleUI, ArticlesAdapter.ViewHolder>(ArticleUIDiff()) {
	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return ViewHolder(LayoutArticlesItemBinding.inflate(inflater))
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItem(position)
		holder.bind(item)
	}
	
	class ViewHolder(private val binding: LayoutArticlesItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: ArticleUI) = with(binding) {
			articleHeader.text = item.header
			articleBody.text = item.description
			Glide.with(root).load(item.image).into(articleIcon)
			root.setOnClickListener { item.onClick() }
		}
	}
}