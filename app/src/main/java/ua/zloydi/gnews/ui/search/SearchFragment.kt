package ua.zloydi.gnews.ui.search

import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.activity.addCallback
import androidx.annotation.AttrRes
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.zloydi.gnews.R
import ua.zloydi.gnews.data.query.Filter
import ua.zloydi.gnews.data.query.Sort
import ua.zloydi.gnews.databinding.FragmentSearchBinding
import ua.zloydi.gnews.ui.article.ArticleFragment
import ua.zloydi.gnews.ui.core.BindingFragment
import ua.zloydi.gnews.ui.core.OneShot
import ua.zloydi.gnews.ui.core.adapters.ArticlesAdapter
import ua.zloydi.gnews.ui.core.adapters.HeaderAdapter
import ua.zloydi.gnews.ui.core.adapters.SearchHistoryAdapter
import ua.zloydi.gnews.ui.core.adapters.decorators.HeaderBodyDecorator
import ua.zloydi.gnews.ui.filter.FilterFragment
import ua.zloydi.gnews.ui.sort.SortFragmentDialog
import ua.zloydi.gnews.utils.Snackbar
import ua.zloydi.gnews.utils.getThemeColor
import kotlin.properties.Delegates

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>() {
	override fun inflateBinding(inflater: LayoutInflater) =
		FragmentSearchBinding.inflate(layoutInflater)
	
	private val viewModel: SearchViewModel by viewModels()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		bindStable()
		lifecycleScope.launchWhenStarted {
			launch { viewModel.state.collect(::bindState) }
			launch { viewModel.searchState.collect(::bindSearchState) }
			launch { viewModel.oneShot.collect(::bindOneShot) }
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
			if (!viewModel.isSearchEmpty()) viewModel.setHistoryScreen()
			else {
				remove()
				requireActivity().onBackPressed()
			}
		}
	}
	
	private var searchHistoryAdapter: SearchHistoryAdapter by Delegates.notNull()
	private var articlesAdapter: ArticlesAdapter by Delegates.notNull()
	
	private fun bindOneShot(oneShot: OneShot) {
		when (oneShot) {
			is OneShot.OpenArticle -> ArticleFragment.open(
				oneShot.article, requireActivity().supportFragmentManager
			)
			is OneShot.ShowError   -> Snackbar.show(oneShot.message, binding.root)
		}
	}
	
	private fun bindStable() = with(binding) {
		searchHistoryAdapter = SearchHistoryAdapter()
		articlesAdapter = ArticlesAdapter()
		adapter.layoutManager = LinearLayoutManager(requireContext())
		
		bindSearchBar()
		
		binding.error.retry.setOnClickListener { viewModel.search() }
	}
	
	private fun bindSearchBar() = with(binding.searchBar) {
		queryLayout.setStartIconOnClickListener {
			search()
		}
		
		queryEditText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				search()
				return@OnEditorActionListener true
			}
			false
		})
		
		queryEditText.addTextChangedListener {
			viewModel.updateQuery(it?.toString() ?: "")
		}
		
		btnFilter.setOnClickListener { filter() }
		btnSort.setOnClickListener { sort() }
	}
	
	private fun bindState(state: State) = when (state) {
		is State.Empty        -> setLoading(true)
		is State.Loading      -> setLoading(true)
		is State.Error        -> setError(state)
		is State.ShowArticles -> {
			bindArticles(state)
			setLoading(false)
		}
		is State.ShowHistory  -> {
			bindHistory(state)
			setLoading(false)
		}
	}
	
	private fun setError(state: State.Error) {
		binding.error.root.isVisible = true
		binding.progress.isVisible = false
		binding.adapter.isVisible = false
		binding.error.header.text = state.message
	}
	
	private fun setLoading(isLoading: Boolean) {
		binding.progress.isVisible = isLoading
		binding.adapter.isVisible = !isLoading
		binding.error.root.isVisible = false
	}
	
	private val historyDecorator: HeaderBodyDecorator by lazy {
		val normalDp = resources.getDimensionPixelSize(R.dimen.normal)
		HeaderBodyDecorator(
			headerRect = Rect(normalDp, normalDp, normalDp, normalDp),
			itemRect = Rect(normalDp, 0, normalDp, 0),
		)
	}
	
	private fun bindHistory(state: State.ShowHistory) = with(binding) {
		adapter.adapter = ConcatAdapter(
			HeaderAdapter(getString(R.string.search_history)), searchHistoryAdapter
		)
		searchHistoryAdapter.submitList(state.history)
		adapter.removeItemDecoration(articleDecorator)
		adapter.removeItemDecoration(historyDecorator)
		adapter.addItemDecoration(historyDecorator)
	}
	
	private val articleDecorator: HeaderBodyDecorator by lazy {
		val normalDp = resources.getDimensionPixelSize(R.dimen.normal)
		val smallDp = resources.getDimensionPixelSize(R.dimen.small)
		HeaderBodyDecorator(
			headerRect = Rect(normalDp, normalDp, normalDp, normalDp),
			itemRect = Rect(normalDp, smallDp / 2, normalDp, smallDp / 2),
		)
	}
	
	private fun bindArticles(state: State.ShowArticles) = with(binding) {
		adapter.adapter = ConcatAdapter(
			HeaderAdapter(state.totalArticles), articlesAdapter
		)
		articlesAdapter.submitList(state.articles)
		adapter.removeItemDecoration(articleDecorator)
		adapter.removeItemDecoration(historyDecorator)
		adapter.addItemDecoration(articleDecorator)
	}
	
	private fun bindSearchState(searchState: SearchedState) = with(binding.searchBar) {
		if (queryEditText.text.toString() != searchState.q) queryEditText.setText(searchState.q)
		filterCnt.text = searchState.filter.toString()
		filterCnt.isVisible = searchState.filter > 0
	}
	
	private fun filter() {
		val filterFragment = FilterFragment.create(viewModel.getFilter())
		requireActivity().supportFragmentManager.commit {
			add(R.id.activityContainer, filterFragment, null)
			addToBackStack(null)
		}
		filterFragment.setFragmentResultListener(FilterFragment.RESULT) { _, res ->
			viewModel.updateFilter(res[FilterFragment.RESULT] as Filter?)
			viewModel.search()
		}
	}
	
	private fun sort() {
		val sortFragment = SortFragmentDialog.create(viewModel.getSort())
		sortFragment.show(parentFragmentManager, null)
		sortFragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
			override fun onStart(owner: LifecycleOwner) {
				super.onStart(owner)
				binding.searchBar.btnSort.backgroundTintList = getColorList(R.attr.colorSecondary)
				binding.searchBar.btnSort.imageTintList = getColorList(R.attr.colorOnSecondary)
			}
			
			override fun onStop(owner: LifecycleOwner) {
				super.onStop(owner)
				binding.searchBar.btnSort.backgroundTintList =
					getColorList(R.attr.colorSurfaceVariant)
				binding.searchBar.btnSort.imageTintList = getColorList(R.attr.colorOnPrimary)
			}
		})
		sortFragment.setFragmentResultListener(SortFragmentDialog.RESULT) { _, res ->
			viewModel.updateSort(res[SortFragmentDialog.RESULT] as Sort)
			viewModel.search()
		}
	}
	
	private fun getColorList(@AttrRes res: Int) =
		ColorStateList.valueOf(requireContext().getThemeColor(res))
	
	private fun search() {
		requireContext().getSystemService<InputMethodManager>()
			?.hideSoftInputFromWindow(binding.searchBar.queryEditText.windowToken, 0)
		binding.searchBar.queryEditText.clearFocus()
		viewModel.search()
	}
}