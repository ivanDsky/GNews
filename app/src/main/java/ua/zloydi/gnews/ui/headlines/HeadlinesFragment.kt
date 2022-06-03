package ua.zloydi.gnews.ui.headlines

import android.view.LayoutInflater
import ua.zloydi.gnews.databinding.FragmentHeadlinesBinding
import ua.zloydi.gnews.ui.core.BindingFragment

class HeadlinesFragment : BindingFragment<FragmentHeadlinesBinding>() {
	override fun inflateBinding(inflater: LayoutInflater) =
		FragmentHeadlinesBinding.inflate(layoutInflater)
	
}