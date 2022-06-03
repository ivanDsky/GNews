package ua.zloydi.gnews.ui.main

import android.view.LayoutInflater
import ua.zloydi.gnews.databinding.FragmentMainBinding
import ua.zloydi.gnews.ui.core.BindingFragment

class MainFragment : BindingFragment<FragmentMainBinding>() {
	override fun inflateBinding(inflater: LayoutInflater) = FragmentMainBinding.inflate(inflater)
	
}