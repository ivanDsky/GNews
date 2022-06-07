package ua.zloydi.gnews.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.commit
import ua.zloydi.gnews.R
import ua.zloydi.gnews.databinding.FragmentMainBinding
import ua.zloydi.gnews.ui.core.BindingFragment
import ua.zloydi.gnews.ui.headlines.HeadlinesFragment
import ua.zloydi.gnews.ui.search.SearchFragment

private const val HEADLINES = "headlines"
private const val SEARCH = "search"

class MainFragment : BindingFragment<FragmentMainBinding>() {
	override fun inflateBinding(inflater: LayoutInflater) = FragmentMainBinding.inflate(inflater)
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		binding.bottomNavigation.setOnItemSelectedListener {
			val fragment = when (it.itemId) {
				R.id.bottomHeadlines -> HEADLINES
				R.id.bottomSearch    -> SEARCH
				else                 -> error("Undefined id")
			}
			childFragmentManager.commit {
				childFragmentManager.fragments.forEach { hide(it) }
				show(childFragmentManager.findFragmentByTag(fragment) ?: return@commit)
			}
			true
		}
		if (savedInstanceState == null) {
			val headlinesFragment = HeadlinesFragment()
			val searchFragment = SearchFragment()
			childFragmentManager.commit {
				replace(R.id.container, headlinesFragment, HEADLINES)
				add(R.id.container, searchFragment, SEARCH)
				hide(searchFragment)
			}
		}
	}
}