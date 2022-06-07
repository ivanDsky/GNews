package ua.zloydi.gnews.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.commit
import ua.zloydi.gnews.R
import ua.zloydi.gnews.databinding.FragmentMainBinding
import ua.zloydi.gnews.ui.core.BindingFragment
import ua.zloydi.gnews.ui.filter.FilterFragment

class MainFragment : BindingFragment<FragmentMainBinding>() {
	override fun inflateBinding(inflater: LayoutInflater) = FragmentMainBinding.inflate(inflater)
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (savedInstanceState == null) {
			childFragmentManager.commit {
				replace(R.id.container, FilterFragment())
			}
		}
	}
}