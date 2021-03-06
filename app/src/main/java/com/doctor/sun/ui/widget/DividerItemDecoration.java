package com.doctor.sun.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 可以传入自定义的drawable的ItemDecoration
 * Created by Lynn on 12/30/15.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    //默认绘制
    private boolean isShowLast = true;

    public DividerItemDecoration(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.listDivider});
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public DividerItemDecoration(Drawable divider, boolean isShowLast) {
        mDivider = divider;
        this.isShowLast = isShowLast;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDivider == null) return;
        if (parent.getChildPosition(view) < 1) return;

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL)
            outRect.top = mDivider.getIntrinsicHeight();
        else outRect.left = mDivider.getIntrinsicWidth();
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent) {
        if (mDivider == null) {
            super.onDrawOver(c, parent);
            return;
        }

        int left = 0, right = 0, top = 0, bottom = 0;
        int childCount = parent.getChildCount();
        int orientation = getOrientation(parent);
        int size = 0;

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                size = mDivider.getIntrinsicHeight();
                top = child.getTop() - params.topMargin;
                bottom = top + size;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else { //horizontal
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();

            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                size = mDivider.getIntrinsicWidth();
                left = child.getLeft() - params.leftMargin;
                right = left + size;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        // show last divider
        if (isShowLast && childCount > 0) {
            View child = parent.getChildAt(childCount - 1);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (orientation == LinearLayoutManager.VERTICAL) {
                top = child.getBottom() + params.bottomMargin;
                bottom = top + size;
            } else { // horizontal
                left = child.getRight() + params.rightMargin;
                right = left + size;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getOrientation();
        } else
            throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
    }

}
