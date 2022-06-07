package ua.zloydi.gnews.ui.core.adapters.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HeaderBodyDecorator(private val headerRect: Rect, private val itemRect: Rect) :
	RecyclerView.ItemDecoration() {
	override fun getItemOffsets(
		outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
	) {
		super.getItemOffsets(outRect, view, parent, state)
		val position = parent.getChildAdapterPosition(view)
		if (position == 0) outRect.set(headerRect)
		else outRect.set(itemRect)
	}
}