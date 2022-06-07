package ua.zloydi.gnews.ui.sort

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import ua.zloydi.gnews.R
import ua.zloydi.gnews.data.query.Sort
import ua.zloydi.gnews.databinding.BottomDialogSortBinding
import ua.zloydi.gnews.databinding.LayoutTitleRadioBinding
import ua.zloydi.gnews.ui.core.BindingBottomSheetFragment

class SortFragmentDialog : BindingBottomSheetFragment<BottomDialogSortBinding>() {
	companion object {
		const val RESULT = "Sort result"
		private const val INPUT = "Sort input"
		
		fun create(sort: Sort): SortFragmentDialog {
			return SortFragmentDialog().apply {
				arguments = bundleOf(INPUT to sort)
			}
		}
	}
	
	private var mapper: Map<Sort, LayoutTitleRadioBinding>? = null
	
	override fun inflateBinding(inflater: LayoutInflater) =
		BottomDialogSortBinding.inflate(inflater)
	
	private val viewModel: SortViewModel by viewModels {
		SortViewModel.Factory(requireArguments()[INPUT] as Sort)
	}
	
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialog = BottomSheetDialog(requireContext(), theme)
		dialog.window?.setDimAmount(0f)
		dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
		return dialog
	}
	
	override fun getTheme() = R.style.BaseBottomSheetDialog
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		bindStable()
		setStyle(STYLE_NORMAL, R.style.BaseBottomSheetDialog)
		lifecycleScope.launchWhenStarted {
			viewModel.state.collect(::bindState)
		}
	}
	
	private fun bindStable() = with(binding) {
		layoutHeader.header.text = getString(R.string.sort_by)
		mapper = mapOf(
			Sort.PUBLISHED_AT to radioPublishedAt, Sort.RELEVANCE to radioRelevance
		)
		mapper?.forEach { bindRadio(it.key, it.value) }
	}
	
	private fun bindRadio(sort: Sort, binding: LayoutTitleRadioBinding) = with(binding) {
		title.text = getString(sort.res)
		root.setOnClickListener { viewModel.setSort(sort) }
		radio.setOnClickListener { viewModel.setSort(sort) }
	}
	
	private fun bindState(sort: SortState) = with(binding) {
		mapper?.forEach {
			it.value.radio.isChecked = false
		}
		mapper?.get(sort.sort)?.radio?.isChecked = true
		if (!sort.isInit) {
			setFragmentResult(RESULT, bundleOf(RESULT to sort.sort))
			dismiss()
		}
	}
	
	override fun onDestroyView() {
		mapper = null
		super.onDestroyView()
	}
}