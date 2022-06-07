package ua.zloydi.gnews.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ua.zloydi.gnews.R

object Snackbar {
	fun show(message: String, view: View) =
		Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
			setBackgroundTint(view.context.getThemeColor(R.attr.colorSecondary))
			setTextColor(view.context.getThemeColor(R.attr.colorOnSecondary))
			show()
		}
}
