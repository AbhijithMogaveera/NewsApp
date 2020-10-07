package com.abhijith.newsapp.ui.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Custom Item Decoration to set top and bottpm offset for showing the list
 */
class RecyclerViewDecoration(private val mHorizontalOffset: Int, private val mVerticalOffset: Int) :
    ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[mHorizontalOffset, mVerticalOffset, mHorizontalOffset] = mVerticalOffset
    }
}