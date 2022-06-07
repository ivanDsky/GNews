package ua.zloydi.gnews.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object Snackbar {
	fun show(message: String, view: View) =
		Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}
