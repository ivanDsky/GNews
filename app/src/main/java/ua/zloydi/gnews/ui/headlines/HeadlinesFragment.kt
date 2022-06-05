package ua.zloydi.gnews.ui.headlines

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ua.zloydi.gnews.R
import ua.zloydi.gnews.databinding.FragmentHeadlinesBinding
import ua.zloydi.gnews.ui.core.BindingFragment
import ua.zloydi.gnews.ui.core.adapters.ArticlesAdapter
import ua.zloydi.gnews.ui.core.adapters.HeaderAdapter
import kotlin.properties.Delegates

@AndroidEntryPoint
class HeadlinesFragment : BindingFragment<FragmentHeadlinesBinding>() {
	override fun inflateBinding(inflater: LayoutInflater) =
		FragmentHeadlinesBinding.inflate(layoutInflater)
	
	private val viewModel: HeadlinesViewModel by viewModels()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		bindStable()
		lifecycleScope.launchWhenStarted {
			viewModel.state.collect(::bindState)
		}
	}
	
	private var articlesAdapter: ArticlesAdapter by Delegates.notNull()
	
	private fun bindStable() = with(binding) {
		articlesAdapter = ArticlesAdapter()
		articles.layoutManager = LinearLayoutManager(requireContext())
		articles.adapter = ConcatAdapter(
			HeaderAdapter(getString(R.string.news)), articlesAdapter
		)
		articles.addItemDecoration(object : RecyclerView.ItemDecoration(){
			override fun getItemOffsets(
				outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
			) {
				super.getItemOffsets(outRect, view, parent, state)
				outRect.set(0,20,0,20)
			}
		})
	}
	
	private fun bindState(state: State) {
		when (state) {
			State.Loading         -> {}
			is State.Error        -> {}
			is State.ShowArticles -> articlesAdapter.submitList(state.articles)
		}
	}
}