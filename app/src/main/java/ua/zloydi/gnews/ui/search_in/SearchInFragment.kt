package ua.zloydi.gnews.ui.search_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import ua.zloydi.gnews.data.query.SearchIn
import ua.zloydi.gnews.databinding.FragmentSearchInBinding
import ua.zloydi.gnews.databinding.LayoutTitleSwitchBinding
import ua.zloydi.gnews.ui.core.BindingFragment

class SearchInFragment : BindingFragment<FragmentSearchInBinding>() {
	companion object {
		const val RESULT = "Search in result"
		private const val INPUT = "Search in input"
		
		fun create(searchInList: List<SearchIn>): SearchInFragment {
			return SearchInFragment().apply {
				arguments = bundleOf(INPUT to searchInList)
			}
		}
	}
	
	private val viewModel: SearchInViewModel by viewModels {
		SearchInViewModel.Factory(requireArguments()[INPUT] as List<SearchIn>)
	}
	
	private var mapper: Map<SearchIn, LayoutTitleSwitchBinding>? = null
	override fun inflateBinding(inflater: LayoutInflater) =
		FragmentSearchInBinding.inflate(inflater)
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		bindStable()
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
			parentFragmentManager.popBackStack()
		}
		lifecycleScope.launchWhenStarted {
			viewModel.state.collect {
				bindState(it)
			}
		}
	}
	
	private fun bindState(state: List<SearchInState>) {
		state.forEach { item ->
			mapper?.get(item.searchIn)?.swtch?.isChecked = item.isEnabled
		}
	}
	
	private fun bindStable() = with(binding) {
		mapper = mapOf(
			SearchIn.TITLE to switchTitle,
			SearchIn.DESCRIPTION to switchDescription,
			SearchIn.CONTENT to switchContent,
		)
		mapper?.forEach { bindSwitch(it.key, it.value) }
		
		btnBack.setOnClickListener { requireActivity().onBackPressed() }
		btnClear.root.setOnClickListener { viewModel.clearAll() }
		btnApply.setOnClickListener { onApply() }
	}
	
	private fun onApply() {
		setFragmentResult(RESULT, bundleOf(RESULT to viewModel.getResult()))
		requireActivity().onBackPressed()
	}
	
	private fun bindSwitch(searchIn: SearchIn, binding: LayoutTitleSwitchBinding) = with(binding) {
		title.text = getString(searchIn.res)
		swtch.setOnCheckedChangeListener { _, isEnabled ->
			viewModel.update(searchIn, isEnabled)
		}
	}
	
	override fun onDestroyView() {
		mapper = null
		super.onDestroyView()
	}
}