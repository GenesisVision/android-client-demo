package vision.genesis.android.ui.views

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class TokenHolderDecoration(private val horizontalSpace: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.left = horizontalSpace
        outRect.right = horizontalSpace
    }
}