package com.doctor.sun.ui.adapter.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doctor.sun.R;


/**
 * ItemDecoration implementation that applies and inset margin
 * around each child of the RecyclerView. It also draws item dividers
 * that are expected from a vertical list implementation, such as
 * ListView.
 */
public class GridDividerDecoration extends RecyclerView.ItemDecoration {

    private int mInsets;

    public GridDividerDecoration(Context context) {
        mInsets = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //We can supply forced insets for each item view here in the Rect
        outRect.set(mInsets, mInsets, 0, 0);
    }
}
