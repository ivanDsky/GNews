package ua.zloydi.gnews.ui.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.zloydi.gnews.databinding.LayoutHeaderBinding

class HeaderAdapter(private val title: String) : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return ViewHolder(LayoutHeaderBinding.inflate(inflater, parent, false), title)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
	
	override fun getItemCount() = 1
	
	class ViewHolder(binding: LayoutHeaderBinding, title: String) :
		RecyclerView.ViewHolder(binding.root) {
		init {
			binding.header.text = title
		}
	}
}