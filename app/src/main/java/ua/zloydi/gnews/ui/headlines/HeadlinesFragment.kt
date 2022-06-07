package ua.zloydi.gnews.ui.headlines

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.zloydi.gnews.R
import ua.zloydi.gnews.databinding.FragmentHeadlinesBinding
import ua.zloydi.gnews.ui.article.ArticleFragment
import ua.zloydi.gnews.ui.core.BindingFragment
import ua.zloydi.gnews.ui.core.OneShot
import ua.zloydi.gnews.ui.core.adapters.ArticlesAdapter
import ua.zloydi.gnews.ui.core.adapters.HeaderAdapter
import ua.zloydi.gnews.ui.core.adapters.decorators.HeaderBodyDecorator
import ua.zloydi.gnews.utils.Snackbar
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
			launch { viewModel.state.collect(::bindState) }
			launch { viewModel.oneShot.collect(::bindOneShot) }
		}
	}
	
	private var articlesAdapter: ArticlesAdapter by Delegates.notNull()
	
	private fun bindStable() = with(binding) {
		articlesAdapter = ArticlesAdapter()
		articles.layoutManager = LinearLayoutManager(requireContext())
		articles.adapter = ConcatAdapter(
			HeaderAdapter(getString(R.string.news)), articlesAdapter
		)
		val normalDp = resources.getDimensionPixelSize(R.dimen.normal)
		val smallDp = resources.getDimensionPixelSize(R.dimen.small)
		articles.addItemDecoration(
			HeaderBodyDecorator(
				headerRect = Rect(normalDp, normalDp, normalDp, normalDp),
				itemRect = Rect(normalDp, smallDp / 2, normalDp, smallDp / 2),
			)
		)
	}
	
	private fun bindState(state: State) {
		when (state) {
			State.Loading         -> setLoading(true)
			is State.ShowArticles -> {
				articlesAdapter.submitList(state.articles)
				setLoading(false)
			}
		}
	}
	
	private fun setLoading(isLoading: Boolean) {
		binding.progress.isVisible = isLoading
		binding.articles.isVisible = !isLoading
	}
	
	private fun bindOneShot(oneShot: OneShot) {
		when (oneShot) {
			is OneShot.OpenArticle -> ArticleFragment.open(
				oneShot.article, requireActivity().supportFragmentManager
			)
			is OneShot.ShowError   -> Snackbar.show(oneShot.message, binding.root)
		}
	}
}