package ua.zloydi.gnews.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BindingBottomSheetFragment<out VB : ViewBinding> : BottomSheetDialogFragment() {
	private var _binding: VB? = null
	protected val binding: VB get() = checkNotNull(_binding)
	
	abstract fun inflateBinding(inflater: LayoutInflater): VB
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = inflateBinding(inflater)
		return binding.root
	}
	
	override fun onDestroyView() {
		_binding = null
		super.onDestroyView()
	}
}