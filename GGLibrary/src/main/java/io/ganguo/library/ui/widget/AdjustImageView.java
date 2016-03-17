package io.ganguo.library.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * automatically adjust height when we assign the width
 * Created by Wilson on 14-3-20.
 */
public class AdjustImageView extends ImageView {
    private int imageWidth;
    private int imageHeight;

    public AdjustImageView(Context context) {
        this(context, null);
    }

    public AdjustImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getImageSize();
    }

    private void getImageSize() {
        Drawable d = this.getDrawable();

        if (d == null) {
            return;
        }
        imageWidth = d.getIntrinsicWidth();
        imageHeight = d.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getImageSize();

        if (imageWidth != 0 && imageWidth != 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width * imageHeight / imageWidth;
            this.setMeasuredDimension(width, height);
        } else {
            this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }

    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

}