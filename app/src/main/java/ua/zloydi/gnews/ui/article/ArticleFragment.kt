package ua.zloydi.gnews.ui.article

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ua.zloydi.gnews.R
import ua.zloydi.gnews.databinding.FragmentArticleBinding
import ua.zloydi.gnews.ui.core.BindingFragment

data class Article(val title: String, val url: String) : java.io.Serializable

class ArticleFragment : BindingFragment<FragmentArticleBinding>() {
	companion object {
		private const val INPUT = "Article input"
		
		fun create(article: Article): ArticleFragment {
			return ArticleFragment().apply {
				arguments = bundleOf(INPUT to article)
			}
		}
		
		fun open(article: Article, fragmentManager: FragmentManager) {
			fragmentManager.commit {
				add(R.id.activityContainer, create(article))
				addToBackStack(null)
			}
		}
	}
	
	override fun inflateBinding(inflater: LayoutInflater) = FragmentArticleBinding.inflate(inflater)
	

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		with(binding) {
			val article = requireArguments()[INPUT] as Article
			header.text = article.title
			webView.loadUrl(article.url)
			
			bindWebSettings()
			bindWebClient()
			
			webView.webChromeClient = object : WebChromeClient() {
				override fun onProgressChanged(view: WebView?, newProgress: Int) {
					super.onProgressChanged(view, newProgress)
					progress.progress = newProgress
				}
			}
			requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
				if (webView.canGoBack()) {
					webView.goBack()
				} else {
					parentFragmentManager.popBackStack()
				}
			}
		}
	}
	
	private fun bindWebClient() = with(binding) {
		webView.webViewClient = object : WebViewClient() {
			override fun shouldOverrideUrlLoading(
				view: WebView?, request: WebResourceRequest?
			): Boolean {
				val url = request?.url?.path ?: return super.shouldOverrideUrlLoading(view, request)
				if (url.startsWith("http://") || url.startsWith("https://")) {
					view?.context?.startActivity(Intent(Intent.ACTION_VIEW, request.url));
					return true
				} else {
					return false
				}
			}
			
			override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
				super.onPageStarted(view, url, favicon)
				progress.isVisible = true
			}
			
			override fun onPageFinished(view: WebView?, url: String?) {
				super.onPageFinished(view, url)
				progress.isVisible = false
			}
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private fun bindWebSettings() = with(binding.webView) {
		clearCache(true);
		clearHistory();
		settings.javaScriptEnabled = true
		settings.javaScriptCanOpenWindowsAutomatically = true
	}
}