package ua.zloydi.gnews.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getThemeColor(@AttrRes color: Int) = with(TypedValue()) {
	theme.resolveAttribute(color, this, true)
	data
}