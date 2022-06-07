package ua.zloydi.gnews.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ua.zloydi.gnews.R
import ua.zloydi.gnews.data.query.SearchIn
import ua.zloydi.gnews.databinding.FragmentFilterBinding
import ua.zloydi.gnews.ui.core.BindingFragment
import ua.zloydi.gnews.ui.search_in.SearchInFragment
import ua.zloydi.gnews.utils.getThemeColor
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class FilterFragment : BindingFragment<FragmentFilterBinding>() {
	companion object {
		const val RESULT = "Filter result"
	}
	
	override fun inflateBinding(inflater: LayoutInflater) =
		FragmentFilterBinding.inflate(layoutInflater)
	
	private val viewModel: FilterViewModel by viewModels()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		bindStable()
		lifecycleScope.launchWhenStarted {
			viewModel.state.collect(::bindState)
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
			parentFragmentManager.popBackStack()
		}
	}
	
	private fun bindStable() = with(binding) {
		layoutHeader.header.text = getString(R.string.filter)
		fromDate.dateHeader.text = getString(R.string.from)
		toDate.dateHeader.text = getString(R.string.to)
		
		fromDate.root.setOnClickListener { onFromDateClick() }
		toDate.root.setOnClickListener { onToDateClick() }
		searchIn.root.setOnClickListener { onSearchInClick() }
		
		btnBack.setOnClickListener { requireActivity().onBackPressed() }
		btnApply.setOnClickListener { onApply() }
		btnClear.root.setOnClickListener { viewModel.clearAll() }
	}
	
	@Suppress("UNCHECKED_CAST")
	private fun onSearchInClick() = lifecycleScope.launchWhenStarted {
		val searchInFragment = SearchInFragment()
		parentFragmentManager.commit {
			add(R.id.container, searchInFragment, null)
			addToBackStack(null)
		}
		searchInFragment.setFragmentResultListener(SearchInFragment.RESULT) { _, res ->
			viewModel.setSearchIn(res[SearchInFragment.RESULT] as List<SearchIn>)
		}
	}
	
	private fun onFromDateClick() = lifecycleScope.launchWhenStarted {
		selectDate(
			date = viewModel.getResult().to, isFrom = true
		)?.let { viewModel.setFromDate(it) }
	}
	
	private fun onToDateClick() = lifecycleScope.launchWhenStarted {
		selectDate(
			date = viewModel.getResult().from, isFrom = false
		)?.let { viewModel.setToDate(it) }
	}
	
	private suspend fun selectDate(date: Date? = null, isFrom: Boolean): Date? =
		suspendCoroutine { cont ->
			val calendarConstraints = CalendarConstraints.Builder().run {
				when {
					date == null -> setValidator(DateValidatorPointBackward.now())
					isFrom       -> setValidator(DateValidatorPointBackward.before(date.time))
					else         -> setValidator(DateValidatorPointForward.from(date.time))
				}
				if (date != null) setOpenAt(date.time)
				build()
			}
			
			val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
				.setTheme(R.style.DatePicker).setCalendarConstraints(calendarConstraints).build()
			
			datePicker.show(childFragmentManager, null)
			datePicker.addOnPositiveButtonClickListener { cont.resume(Date(it)) }
			datePicker.addOnCancelListener { cont.resume(null) }
			datePicker.addOnNegativeButtonClickListener { cont.resume(null) }
		}
	
	private fun onApply() {
		setFragmentResult(RESULT, bundleOf(RESULT to viewModel.getResult()))
		requireActivity().onBackPressed()
	}
	
	private fun bindState(state: FilterState) = with(binding) {
		fromDate.inputDate.bindText(state.from, R.string.hint_date)
		toDate.inputDate.bindText(state.to, R.string.hint_date)
		searchIn.selectedSearchIn.bindText(state.searchIn, R.string.all)
	}
	
	private fun TextView.bindText(text: String?, @StringRes defaultRes: Int) {
		if (text == null) {
			this.text = getString(defaultRes)
			setTextColor(requireContext().getThemeColor(R.attr.colorOnPrimarySurface))
		} else {
			this.text = text
			setTextColor(requireContext().getThemeColor(R.attr.colorOnPrimary))
		}
	}
}